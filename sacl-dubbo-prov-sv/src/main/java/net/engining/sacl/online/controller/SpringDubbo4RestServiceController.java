/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.engining.sacl.online.controller;

import io.swagger.annotations.ApiOperation;
import com.alibaba.cloud.dubbo.service.RestService;
import net.engining.sacl.online.service.User;
import net.engining.sacl.online.util.LoggerUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring MVC {@link RestService}
 *
 */
@Service(version = "1.0.0")
@RestController
public class SpringDubbo4RestServiceController implements RestService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	@ApiOperation(value = "param", notes = "")
	@GetMapping(value = "/param")
	public String param(@RequestParam String param) {
		LoggerUtils.log(logger,"/param", param);
		return param;
	}

	@Override
	@ApiOperation(value = "params", notes = "")
	@PostMapping("/params")
	public String params(@RequestParam int a, @RequestParam String b) {
		LoggerUtils.log(logger,"/params", a + b);
		return a + b;
	}

	@Override
	@ApiOperation(value = "headers", notes = "")
	@GetMapping("/headers")
	public String headers(@RequestHeader("h") String header,
                          @RequestHeader("h2") String header2, @RequestParam("v") Integer param) {
		String result = header + " , " + header2 + " , " + param;
		LoggerUtils.log(logger,"/headers", result);
		return result;
	}

	@Override
	@ApiOperation(value = "path-variables", notes = "")
	@GetMapping("/path-variables/{p1}/{p2}")
	public String pathVariables(@PathVariable("p1") String path1,
                                @PathVariable("p2") String path2, @RequestParam("v") String param) {
		String result = path1 + " , " + path2 + " , " + param;
		LoggerUtils.log(logger,"/path-variables", result);
		return result;
	}

	@Override
	@ApiOperation(value = "form", notes = "")
	@PostMapping("/form")
	public String form(@RequestParam("f") String form) {
		return String.valueOf(form);
	}

	@Override
	@ApiOperation(value = "requestBodyMap", notes = "")
	@PostMapping(value = "/request/body/map", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public User requestBodyMap(@RequestBody Map<String, Object> data,
							   @RequestParam("param") String param) {
		User user = new User();
		user.setUserId(((Integer) data.get("id")).longValue());
		user.setName((String) data.get("name"));
		user.setAge((Integer) data.get("age"));
		LoggerUtils.log(logger,"/request/body/map", user);
		return user;
	}

	@Override
	@ApiOperation(value = "requestBodyUser", notes = "")
	@PostMapping(value = "/request/body/user", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, Object> requestBodyUser(@RequestBody User user) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", user.getUserId());
		map.put("name", user.getName());
		map.put("age", user.getAge());
		LoggerUtils.log(logger,"/request/body/user", map);
		return map;
	}

}
