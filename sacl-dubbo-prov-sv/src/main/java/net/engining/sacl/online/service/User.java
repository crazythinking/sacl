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
package net.engining.sacl.online.service;

import java.io.Serializable;

/**
 * User Entity
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class User implements Serializable {

	/**
	 * spring cloud bus 会默认构造RemoteApplicationEvent，其中已包含id字段，因此payload不能使用再有id作为字段；
	 * 容易引起json转换时的冲突；
	 */
	//private Long id;
	private Long userId;

	private String name;

	private Integer age;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + userId + ", name='" + name + '\'' + ", age=" + age + '}';
	}
}
