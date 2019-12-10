package net.engining.sacl.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author luxue
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FcGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FcGatewayApplication.class, args);
	}
	
	@Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
        		/**
        		 * 第一个参数id，不指定默认uuid；
        		 * 第二个参数是一个自定义函数Function<PredicateSpec, Route.AsyncBuilder>，输入是:：PredicateSpec用于定义路由的Predicate，输出是：Predicate的AsyncBuilder
        		 * 第一层（输入）指定各类PredicateFactory，以及各类FilterFactory（如果需要）;
        		 * 第二层（输出）最终指定Predicate的AsyncBuilder作为输出；
        		 * 以下路由配置的含义：path=/163的请求，都在request header上加上headkey: headvalue，然后转向访问https://www.163.com/
        		 * 
        		 */
                .route(
                		"route1",
                		r->r.path("/163")
                				.filters(
                						f->f.addRequestHeader("headkey", "headvalue")
                						)
                			.uri("https://www.163.com/")//最终符合条件转向的URI
                )
				.route(
						"route2",
						r->r.path("/toutiao")
							.uri("https://www.toutiao.com/")
				)
				.build();
    }
}
