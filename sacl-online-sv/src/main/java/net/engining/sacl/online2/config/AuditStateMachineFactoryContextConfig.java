package net.engining.sacl.online2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.data.redis.RedisPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.redis.RedisStateMachineRepository;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

/**
 * 状态机配置；
 * EnableStateMachine 用在每个状态机配置类上，标识该配置类是一个状态机
 *
 * @author Eric Lu
 * @create 2019-08-02 16:05
 **/
@EnableStateMachineFactory(name = {AuditStateMachineFactoryContextConfig.AUDIT_STAT_MACHINE_FACTORY}, contextEvents = false)
public class AuditStateMachineFactoryContextConfig extends EnumStateMachineConfigurerAdapter<AuditStates, AuditEvents> {

    /**logger*/
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditStateMachineFactoryContextConfig.class);
    public static final String AUDIT_STAT_MACHINE_FACTORY = "audit-stat-machine-factory";
    public static final String AUDIT_STAT_MACHINE_1 = "audit-stat-machine-1";

    @Autowired
    @Qualifier("startAction")
    Action<AuditStates, AuditEvents> startAction;

    @Autowired
    @Qualifier("processAction")
    Action<AuditStates, AuditEvents> processAction;

    @Autowired
    @Qualifier("endAction")
    Action<AuditStates, AuditEvents> endAction;

    @Autowired
    @Qualifier("bStateGuard")
    Guard<AuditStates, AuditEvents> bStateGuard;

    @Autowired
    @Qualifier("fStateGuard")
    Guard<AuditStates, AuditEvents> fStateGuard;

    @Autowired
    StateMachineListener<AuditStates, AuditEvents> listener;

    @Autowired
    BeanFactory beanFactory;

    @Autowired
    private StateMachineRuntimePersister<AuditStates, AuditEvents, String> stateMachineRuntimePersister;

    @Configuration
    @Profile("statemachine.jpa")
    public static class JpaPersisterConfig {

        @Bean
        public StateMachineRuntimePersister<AuditStates, AuditEvents, String> stateMachineRuntimePersister(
                JpaStateMachineRepository jpaStateMachineRepository) {
            return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
        }

    }

    @Configuration
    @Profile("statemachine.redis")
    public static class RedisPersisterConfig {

        @Bean
        public StateMachineRuntimePersister<AuditStates, AuditEvents, String> stateMachineRuntimePersister(
                RedisStateMachineRepository redisStateMachineRepository) {
            return new RedisPersistingStateMachineInterceptor<>(redisStateMachineRepository);
        }
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<AuditStates, AuditEvents> machine) throws Exception {

        machine
                .withPersistence()
                    .runtimePersister(stateMachineRuntimePersister)
                .and()
                .withConfiguration()
                    .listener(listener)
                    .beanFactory(beanFactory)
        ;
    }

    /**
     * 定义状态机的初始状态
     *
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<AuditStates, AuditEvents> states)
            throws Exception {
        states
                .withStates()
                .initial(AuditStates.P, startAction)
                .end(AuditStates.F)
                .states(EnumSet.allOf(AuditStates.class));
    }

    /**
     * 绑定状态机 Event 与 State 的触发关系
     *
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<AuditStates, AuditEvents> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(AuditStates.P).target(AuditStates.B).event(AuditEvents.DOING)
                .guard(bStateGuard).action(processAction)
                .and()
                .withExternal()
                .source(AuditStates.B).target(AuditStates.F).event(AuditEvents.COMPLETE)
                .guard(fStateGuard).action(endAction);
    }

    @Override
    public void configure(StateMachineModelConfigurer<AuditStates, AuditEvents> model) throws Exception {
        super.configure(model);
    }

    @Bean
    public StateMachineService<AuditStates, AuditEvents> stateMachineService(
            @Qualifier(AuditStateMachineFactoryContextConfig.AUDIT_STAT_MACHINE_FACTORY)
            StateMachineFactory<AuditStates, AuditEvents> stateMachineFactory,
            StateMachineRuntimePersister<AuditStates, AuditEvents, String> stateMachineRuntimePersister) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
    }

    /**
     * 配置状态机监听器
     *
     * @return
     */
    @Bean
    public StateMachineListener<AuditStates, AuditEvents> listener() {
        return new StateMachineListenerAdapter<AuditStates, AuditEvents>() {
            @Override
            public void stateChanged(State<AuditStates, AuditEvents> from, State<AuditStates, AuditEvents> to) {
                LOGGER.debug("状态变更为------------{}", to.getStates());
            }
        };
    }

    @Bean
    public Action<AuditStates, AuditEvents> startAction() {
        return new Action() {

            @Override
            public void execute(StateContext context) {
                LOGGER.debug("{}------------初始，待处理", context.getStateMachine().getId());

            }
        };

    }

    @Bean
    public Action<AuditStates, AuditEvents> processAction() {
        return new Action() {

            @Override
            public void execute(StateContext context) {
                LOGGER.debug("{}------------开始处理", context.getStateMachine().getId());

            }
        };
    }

    @Bean
    public Action<AuditStates, AuditEvents> endAction() {
        return new Action() {

            @Override
            public void execute(StateContext context) {
                LOGGER.debug("{}------------完成处理", context.getStateMachine().getId());

            }
        };
    }

    @Bean
    public Guard<AuditStates, AuditEvents> bStateGuard(){
        return new Guard<AuditStates, AuditEvents>() {
            @Override
            public boolean evaluate(StateContext<AuditStates, AuditEvents> context) {
                //此处的逻辑只是做个简单例子；config中通常已经定义了source->target，框架本身的执行逻辑已经做了这层判定；
                //见AbstractStateMachine#acceptEvent
                return context.getSource().getId().equals(AuditStates.P);
            }
        };
    }

    @Bean
    public Guard<AuditStates, AuditEvents> fStateGuard(){
        return new Guard<AuditStates, AuditEvents>() {
            @Override
            public boolean evaluate(StateContext<AuditStates, AuditEvents> context) {
                return context.getSource().getId().equals(AuditStates.B);
            }
        };
    }

}
