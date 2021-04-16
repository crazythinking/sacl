package net.engining.sacl.online2.config;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachine;
import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachinePersist;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;

/**
 * 用于重载状态机的自定义持久化逻辑，主要是#read 和 #write 方法；
 * 此类可作为JpaPersistingStateMachineInterceptor构造函数的入参
 * @author : Eric Lu
 * @version :
 * @date : 2021-04-06 16:41
 * @since :
 **/
public class AuditJpaRepositoryStateMachinePersist extends JpaRepositoryStateMachinePersist<AuditStates, AuditEvents> {

    public AuditJpaRepositoryStateMachinePersist(JpaStateMachineRepository jpaStateMachineRepository) {
        super(jpaStateMachineRepository);
    }

    @Override
    protected JpaRepositoryStateMachine build(StateMachineContext<AuditStates, AuditEvents> context, Object contextObj, byte[] serialisedContext) {
        JpaRepositoryStateMachine jpaRepositoryStateMachine = new JpaRepositoryStateMachine();
        //contextObj.toString() 其实就是状态机的实例Id
        jpaRepositoryStateMachine.setMachineId(context.getId());
        jpaRepositoryStateMachine.setState(context.getState() != null ? context.getState().toString() : null);
        jpaRepositoryStateMachine.setStateMachineContext(serialisedContext);
        return jpaRepositoryStateMachine;
    }
}
