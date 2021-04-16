package net.engining.sacl.online2.state.persist;

import net.engining.pg.parameter.entity.dto.ParameterAuditDto;
import net.engining.pg.parameter.entity.enums.AuditOptDef;
import net.engining.sacl.online2.config.AuditEvents;
import net.engining.sacl.online2.config.AuditStateMachineFactoryContextConfig;
import net.engining.sacl.online2.config.AuditStates;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;

/**
 * 一般的设计都会把状态存到业务表里面，而状态机的主要作用其实是规范业务流程的状态和事件，所以状态机要不要保存真的不重要，
 * 只需要从表里面把状态取出来，知道当前是什么状态，然后伴随着业务继续流到下一个状态节点就好了
 *
 * @author : Eric Lu
 * @version :
 * @date : 2020-04-17 19:47
 * @since :
 **/
public class AuditStateMachinePersist implements StateMachinePersist<AuditStates, AuditEvents, ParameterAuditDto> {

    /**
     * 这里不做任何持久化工作，交给业务服务处理
     * @param context
     * @param contextObj
     * @throws Exception
     */
    @Override
    public void write(StateMachineContext<AuditStates, AuditEvents> context, ParameterAuditDto contextObj) throws Exception {

    }

    @Override
    public StateMachineContext<AuditStates, AuditEvents> read(ParameterAuditDto contextObj) throws Exception {
        AuditStates auditStates = null;
        if (contextObj.getAuditOpt().equals(AuditOptDef.AUDIT)){
            auditStates = AuditStates.P;
        }
        else if (contextObj.getAuditOpt().equals(AuditOptDef.CONF)){
            auditStates = AuditStates.F;
        }
        else if (contextObj.getAuditOpt().equals(AuditOptDef.FALLBK)){
            auditStates = AuditStates.B;
        }

        //从业务数据中获取状态机的当前状态
        StateMachineContext<AuditStates, AuditEvents> result =
                new DefaultStateMachineContext<AuditStates, AuditEvents>(
                        auditStates,
                        null,
                        null,
                        null,
                        null,
                        AuditStateMachineFactoryContextConfig.AUDIT_STAT_MACHINE_1
                );
        return result;
    }
}
