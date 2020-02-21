package net.engining.sacl.online.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eric Lu
 * @create 2019-11-20 14:29
 **/
@RestController
public class IndexController {

    @ApiOperation(value = "index", notes = "")
    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }
}
