package net.engining.sacl.online2.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-04-17 9:51
 * @since :
 **/
@Component
public class AuditStateMachineBuilder {

    public final static String STATE_MACHINE_ID = "audit-stat-machine";

    /**
     * 构建状态机
     *
     * @param beanFactory
     * @return
     * @throws Exception
     */
    public StateMachine<AuditStates, AuditEvents> build(BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<AuditStates, AuditEvents> builder = StateMachineBuilder.builder();

        //基本配置
        builder.configureConfiguration()
                .withConfiguration()
                .machineId(STATE_MACHINE_ID)
                .beanFactory(beanFactory)
        ;

        //状态配置
        builder.configureStates()
                .withStates()
                .initial(AuditStates.P)
                .states(EnumSet.allOf(AuditStates.class))
        ;

        //状态移动配置
        builder.configureTransitions()
                .withExternal()
                    .source(AuditStates.P).target(AuditStates.B).event(AuditEvents.P)
                .and()
                .withExternal()
                    .source(AuditStates.B).target(AuditStates.F).event(AuditEvents.B)
        ;

        return builder.build();
    }
}
