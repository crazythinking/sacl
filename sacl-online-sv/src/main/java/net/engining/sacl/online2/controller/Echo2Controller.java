package net.engining.sacl.online2.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping(value={"/sentest"})
public class Echo2Controller {

    @Autowired
    EchoService echoService;

    @ApiOperation(value = "test for object", notes = "")
    @GetMapping(value = "/echoObj2")
    public List<Foo> echo2() throws Exception{

        return Lists.newArrayList(echoService.foo1(), echoService.foo2());

    }
}
