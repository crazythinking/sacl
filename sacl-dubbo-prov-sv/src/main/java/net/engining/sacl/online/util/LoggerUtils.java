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
package net.engining.sacl.online.util;

import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;

/**
 * Logger Utilities
 */
public abstract class LoggerUtils {

	public static void log(Logger logger, String url, Object result) {
		if (logger.isInfoEnabled()) {
			logger.info(
					"The client[{}] uses '{}' protocol to call {} : {}",
					RpcContext.getContext().getRemoteHostName(),
					RpcContext.getContext().getUrl() == null ? "N/A" : RpcContext.getContext().getUrl().getProtocol(),
					url,
					result
			);
		}
	}
}
