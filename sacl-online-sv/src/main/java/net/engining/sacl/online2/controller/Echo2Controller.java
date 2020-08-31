package net.engining.sacl.online2.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.sacl.online2.config.AuditEvents;
import net.engining.sacl.online2.config.AuditStateMachineBuilder;
import net.engining.sacl.online2.config.AuditStates;
import net.engining.sacl.online2.sao.SaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2019-12-26 10:31
 * @since :
 **/
@RestController
@RequestMapping(value={"/sentest"})
public class Echo2Controller {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(Echo2Controller.class);

    @Autowired
    EchoService echoService;

    @Autowired
    SaoService saoService;

    @Autowired
    AuditStateMachineBuilder auditStateMachineBuilder;

    @Autowired
    private BeanFactory beanFactory;

    @ApiOperation(value = "test for object", notes = "")
    @GetMapping(value = "/echoObj2")
    public List<Foo> echo2() throws Exception{

        return Lists.newArrayList(echoService.foo1(), echoService.foo2());

    }

    @ApiOperation(value = "test for object", notes = "")
    @GetMapping(value = "/echoObj2Feign")
    public List<Foo> echo2Feign() throws Exception{

        return saoService.echo2();

    }

    @ApiOperation(value = "test for state machine", notes = "")
    @GetMapping(value = "/audit")
    public void audit() throws Exception{

        StateMachine<AuditStates, AuditEvents> stateMachine = auditStateMachineBuilder.build(beanFactory);
        LOGGER.debug(stateMachine.getId());

    }

    @ApiOperation(value = "状态机-开始处理", notes = "")
    @RequestMapping(value = "/startProcess", method = RequestMethod.POST)
    public CommonWithHeaderResponse<Void,Void> startProcess() throws Exception {
        StateMachine<AuditStates, AuditEvents> stateMachine = auditStateMachineBuilder.build(beanFactory);
        LOGGER.debug(stateMachine.getId());

        stateMachine.start();

        stateMachine.sendEvent(AuditEvents.P);

        stateMachine.sendEvent(AuditEvents.B);

        return new CommonWithHeaderResponseBuilder<Void,Void>()
                .build();
    }

//    @ApiOperation(value = "状态机-完成处理", notes = "")
//    @RequestMapping(value = "/finishProcess", method = RequestMethod.POST)
//    public CommonWithHeaderResponse<Void,Void> finishProcess(){
//
//        return new CommonWithHeaderResponseBuilder<Void,Void>()
//                .build();
//    }
}
