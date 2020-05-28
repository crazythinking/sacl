package net.engining.sacl.online2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * 审计状态机配置；
 * EnableStateMachine 用在每个状态机配置类上，标识该配置类是一个状态机
 *
 * @author Eric Lu
 * @create 2019-08-02 16:05
 **/
@Configuration
@EnableStateMachine
public class AuditStateConfig extends EnumStateMachineConfigurerAdapter<AuditStates, AuditEvents> {

    public final static String STATE_MACHINE_ID = "audit-stat-machine";

    /**
     * 定义状态机自动随服务启动，并设置监听
     * @param config
     * @throws Exception
     */
    @Override
    public void configure(StateMachineConfigurationConfigurer<AuditStates, AuditEvents> config)
            throws Exception {
        config
                .withConfiguration()
                .machineId("audit-stat-machine")
        ;
    }

    /**
     * 定义状态机的初始状态
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<AuditStates, AuditEvents> states)
            throws Exception {
        states
                .withStates()
                .initial(AuditStates.P)
                .states(EnumSet.allOf(AuditStates.class));
    }

    /**
     * 绑定状态机 Event 与 State 的触发关系
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<AuditStates, AuditEvents> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(AuditStates.P).target(AuditStates.B).event(AuditEvents.P)
                .and()
                .withExternal()
                .source(AuditStates.B).target(AuditStates.F).event(AuditEvents.B);
    }

//    /**
//     * 配置状态机监听器
//     * @return
//     */
//    @Bean
//    public StateMachineListener<AuditStates, AuditEvents> listener() {
//        return new StateMachineListenerAdapter<AuditStates, AuditEvents>() {
//            @Override
//            public void stateChanged(State<AuditStates, AuditEvents> from, State<AuditStates, AuditEvents> to) {
//                System.out.println(String.format("State change to %s", to.getId()));
//            }
//        };
//    }

}
