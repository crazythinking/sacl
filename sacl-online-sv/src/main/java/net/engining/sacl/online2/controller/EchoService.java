package net.engining.sacl.online2.controller;

import com.google.common.collect.Maps;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.db.id.generator.SnowflakeSequenceID;
import net.engining.pg.support.utils.ExceptionUtilsExt;
import net.engining.pg.support.utils.ValidateUtilExt;
import net.engining.sacl.online2.config.AuditEvents;
import net.engining.sacl.online2.config.AuditStateMachineFactoryContextConfig;
import net.engining.sacl.online2.config.AuditStates;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.Lifecycle;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

//@Service
public class EchoService {
    /** logger */
    private static final Logger log = LoggerFactory.getLogger(EchoService.class);

    @Autowired
    @Qualifier(AuditStateMachineFactoryContextConfig.AUDIT_STAT_MACHINE_FACTORY)
    private StateMachineFactory<AuditStates, AuditEvents> stateMachineFactory;

    @Autowired
    private StateMachineService<AuditStates, AuditEvents> stateMachineService;

    @Autowired
    private SnowflakeSequenceID snowflakeSequenceId;

    private Map<String, StateMachine<AuditStates, AuditEvents>> stateMachineMap = Maps.newConcurrentMap();

    public Map<String, StateMachine<AuditStates, AuditEvents>> getStateMachineMap() {
        return stateMachineMap;
    }

    public Foo foo1() throws InterruptedException {
        Foo foo = new Foo();
        foo.setF1("eric1");
        foo.setF2(new BigDecimal(111));
        log.info("foo1 will return foo after 1s, @[{}]", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        Thread.sleep(1000L);
        return foo;
    }

    public Foo foo2() throws InterruptedException {
        Foo foo = new Foo();
        foo.setF1("eric2");
        foo.setF2(new BigDecimal(222));
        log.info("foo2 will return foo after 2s, @[{}]", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        Thread.sleep(2000L);
        return foo;
    }

    @Async
    public void foo3() throws InterruptedException {
        Foo foo = new Foo();
        foo.setF1("eric2");
        foo.setF2(new BigDecimal(222));
        Thread.sleep(2000L);
        log.info("foo3 return foo @[{}]", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    public String start(){
        //启动时生成唯一的状态机实例ID
        //StateMachine<AuditStates, AuditEvents> stateMachine = stateMachineFactory.getStateMachine(
        //        snowflakeSequenceId.nextIdString()
        //);
        StateMachine<AuditStates, AuditEvents> stateMachine = stateMachineService.acquireStateMachine(
                snowflakeSequenceId.nextIdString()
        );
        return stateMachine.getId();
    }

    public void process(String id, AuditEvents event){
        //只获取不主动启动，如果与start动作不在一个进程内触发，此时running=false
        StateMachine<AuditStates, AuditEvents> stateMachine = stateMachineService.acquireStateMachine(id,false);
        if (ValidateUtilExt.isNotNullOrEmpty(stateMachine)) {
            //如果此时状态机已经是完成状态则不再触发任何动作
            if (!stateMachine.isComplete()){
                //不在运行状态
                if (!((Lifecycle) stateMachine).isRunning()){
                    //再获取一次，此时是从内存中直接获取，但需要触发start动作
                    stateMachine = stateMachineService.acquireStateMachine(id);
                }
                stateMachine.sendEvent(event);
            }
            else {
                log.debug("当前状态机{}已经处理完成，不可再被触发", id);
            }
        }
    }
}
