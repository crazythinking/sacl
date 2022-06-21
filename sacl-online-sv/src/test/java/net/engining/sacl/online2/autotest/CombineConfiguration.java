package net.engining.sacl.online2.autotest;

import net.engining.pg.support.core.context.ApplicationContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

@Configuration
@ComponentScan(basePackages = {
		"net.engining.sacl.online2.metrics",
		//"net.engining.sacl.online2.bus",
})
public class CombineConfiguration {

	@Bean
	@Lazy(value=false)
	public ApplicationContextHolder applicationContextHolder(){
		return new ApplicationContextHolder();
	}

}
