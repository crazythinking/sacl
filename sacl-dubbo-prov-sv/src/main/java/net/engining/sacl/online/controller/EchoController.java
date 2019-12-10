package net.engining.sacl.online.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import net.engining.pg.web.CommonResponseBuilder;
import net.engining.pg.web.bean.BaseResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Eric Lu
 */
@RestController
@RequestMapping(value={"/learnNacos"})
public class EchoController {

	@Autowired
	EchoService echoService;

	@ApiOperation(value = "Hello，Nacos Discovery", notes = "")
	@PreAuthorize("hasAuthority('ProfileBranch')")
	@GetMapping(value = "/echo/{string}")
	public BaseResponseBean echo(@PathVariable String string) {

		return new CommonResponseBuilder()
				.build()
				.putAdditionalRepMap("say", "hello Nacos Discovery " + string);
	}

	@ApiOperation(value = "除法计算", notes = "")
	@PreAuthorize("hasAuthority('ProfileRole')")
	@GetMapping(value = "/divide")
	public BaseResponseBean divide(@RequestParam Integer a, @RequestParam Integer b) {
		return new CommonResponseBuilder()
				.build()
				.putAdditionalRepMap("say", String.valueOf(a / b));
	}

	@ApiOperation(value = "flux test for object", notes = "")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping(value = "/echoObj")
	public List<Foo> echo1(@RequestBody Foo foo) throws Exception{

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

	static class Foo extends BaseResponseBean {
		String f1;
		BigDecimal f2;

		public String getF1() {
			return f1;
		}

		public void setF1(String f1) {
			this.f1 = f1;
		}

		public BigDecimal getF2() {
			return f2;
		}

		public void setF2(BigDecimal f2) {
			this.f2 = f2;
		}
	}
}
