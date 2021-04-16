package net.engining.sacl.online2.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.support.db.id.generator.SnowflakeSequenceID;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.sacl.online2.config.AuditEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2019-12-26 10:31
 * @since :
 **/
@RestController
@RequestMapping(value={"/statemachine"})
public class Echo2Controller {

    @Autowired
    private EchoService echoService;

    @Autowired
    private RetryEchoService retryEchoService;

    @ApiOperation(value = "test for state machine", notes = "")
    @GetMapping(value = "/audit-start")
    public CommonWithHeaderResponse<Void,String> audit() throws Exception{
        String uuid = echoService.start();
        return new CommonWithHeaderResponseBuilder<Void,String>()
                .build()
                .setResponseData(uuid);
    }

    @ApiOperation(value = "test for state machine", notes = "")
    @GetMapping(value = "/audit-do/{uuid}")
    public void auditDoing(@PathVariable String uuid) throws Exception{
        echoService.process(uuid, AuditEvents.DOING);
    }

    @ApiOperation(value = "test for state machine", notes = "")
    @GetMapping(value = "/audit-complete/{uuid}")
    public void auditComplete(@PathVariable String uuid) throws Exception{
        echoService.process(uuid, AuditEvents.COMPLETE);
    }

    @ApiOperation(value = "test for aysnc", notes = "")
    @GetMapping(value = "/aysnc")
    public void echo3() throws Exception{
        echoService.foo3();
    }

    @ApiOperation(value = "test for retry", notes = "")
    @GetMapping(value = "/retry/{key}")
    public Foo echo4(@PathVariable String key) throws Exception{
        return retryEchoService.foo4(key);
    }

}
