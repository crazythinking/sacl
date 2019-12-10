package net.engining.sacl.online.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eric Lu
 * @create 2019-11-20 14:29
 **/
@RestController
public class IndexController {

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }
}
