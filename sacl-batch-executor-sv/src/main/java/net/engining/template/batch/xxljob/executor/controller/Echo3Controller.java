package net.engining.template.batch.xxljob.executor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2019-12-26 10:31
 * @since :
 **/
@RestController
@RequestMapping(value={"/groovy"})
public class Echo3Controller {

    @Autowired
    JobHandlerGroovyService jobHandlerGroovyService;

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(Echo3Controller.class);

    @ApiOperation(value = "test for groovy", notes = "")
    @PostMapping("/publish")
    public void publish(@RequestParam String path) throws IllegalAccessException, InstantiationException {

        jobHandlerGroovyService.loadGroovyJobHandlerFromFile(path);


    }


}
