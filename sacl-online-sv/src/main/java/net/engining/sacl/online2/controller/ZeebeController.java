package net.engining.sacl.online2.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    ZeebeService zeebeService;

    @ApiOperation(value = "test for zeebe, start process", notes = "")
    @PostMapping("/start")
    public boolean start() {
        zeebeService.startProcesses();
        return true;
    }

}
