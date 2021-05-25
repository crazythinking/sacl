package net.engining.sacl.online2.controller;

import io.swagger.annotations.ApiOperation;
import net.engining.zeebe.spring.client.ext.ZeebeSimpleAdminHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2021-04-16 14:12
 * @since :
 **/
@RestController
@RequestMapping(value={"/zeebe"})
public class ZeebeController {

    @Autowired
    DemoProcessStarterService demoProcessStarterService;

    @Autowired
    ZeebeSimpleAdminHandler zeebeSimpleAdminHandler;

    @ApiOperation(value = "test for zeebe, start process", notes = "")
    @PostMapping("/start")
    public boolean start() {
        demoProcessStarterService.startProcess();
        return true;
    }

    @ApiOperation(value = "test for zeebe, get topology", notes = "")
    @GetMapping("/topology")
    public void topology() {
        zeebeSimpleAdminHandler.getTopology();
    }

}
