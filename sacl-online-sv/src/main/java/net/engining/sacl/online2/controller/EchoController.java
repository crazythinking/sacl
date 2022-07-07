package net.engining.sacl.online2.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import org.apache.shenyu.client.springcloud.annotation.ShenyuSpringCloudClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Eric Lu
 */
@RestController
@RequestMapping(value={"/learnNacos"})
//@ShenyuSpringCloudClient(path = "/learnNacos/**")
public class EchoController {

	@Autowired
	EchoService echoService;

	public static final String A = "";

	@ApiOperation(value = "Hello，Nacos Discovery", notes = "")
	//@ShenyuSpringCloudClient(path = "/echo/**")
	@GetMapping(value = "/echo/{string}")
	public CommonWithHeaderResponse<Void, Void> echo(@PathVariable String string) {

		return new CommonWithHeaderResponseBuilder<Void, Void>()
				.build()
				.putAdditionalRepMap("say", "hello Nacos Discovery " + string);
	}

	@ApiOperation(value = "除法计算", notes = "")
	@PreAuthorize("hasAuthority('ProfileRole')")
	@GetMapping(value = "/divide")
	public CommonWithHeaderResponse<Void, Void> divide(@RequestParam Integer a, @RequestParam Integer b) {
		return new CommonWithHeaderResponseBuilder<Void, Void>()
				.build()
				.putAdditionalRepMap("say", String.valueOf(a / b));
	}

	@ApiOperation(value = "test for object", notes = "")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping(value = "/echoObj")
	public List<Foo> echo1() throws Exception{

		return Lists.newArrayList(echoService.foo1(), echoService.foo2());

	}

	@GetMapping(value = "get")
	@PreAuthorize("hasAuthority('ProfileUser')")
	public Object get(Authentication authentication){
		authentication.getCredentials();
		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)authentication.getDetails();
		String token = details.getTokenValue();
		return token;
	}

}
