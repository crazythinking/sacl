package net.engining.sacl.online2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

/**
 * @author Eric Lu
 * @create 2019-08-02 17:53
 **/

@WithStateMachine(id = AuditStateMachineBuilder.STATE_MACHINE_ID)
public class AuditEventConfig {

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(AuditEventConfig.class);

    @OnTransition(target = "P")
    public void start(){
        log.debug("------------初始，待处理");
    }

    @OnTransition(source = "P", target = "B")
    public void process(){
        log.debug("------------开始处理");
    }

    @OnTransition(source = "B", target = "F")
    public void end(){
        log.debug("------------完成处理");
    }
}
