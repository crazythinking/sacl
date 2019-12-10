package net.engining.sacl.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * @author Eric Lu
 */
@EnableDiscoveryClient
@EnableAuthorizationServer
@SpringBootApplication
public class OauthSvApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(OauthSvApplication.class, args);
	}

}
