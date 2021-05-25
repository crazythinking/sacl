package net.engining.sacl.online2.config;

import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2021-04-16 16:46
 * @since :
 **/
@Configuration
@ZeebeDeployment(classPathResources = {
        "demoProcess.bpmn",
        "order-process.bpmn"
})
public class ZeebeContextConfig {

}
