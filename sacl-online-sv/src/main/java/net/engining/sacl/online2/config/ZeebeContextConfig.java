package net.engining.sacl.online2.config;

import io.zeebe.spring.client.EnableZeebeClient;
import io.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2021-04-16 16:46
 * @since :
 **/
@Configuration
@EnableZeebeClient
@ZeebeDeployment(classPathResources = "demoProcess.bpmn")
public class ZeebeContextConfig {

}
