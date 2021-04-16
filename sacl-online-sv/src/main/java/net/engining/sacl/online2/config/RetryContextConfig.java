package net.engining.sacl.online2.config;

import net.engining.pg.support.utils.ExceptionUtilsExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.listener.RetryListenerSupport;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2021-04-09 13:56
 * @since :
 **/
@Configuration
@EnableRetry(proxyTargetClass = true)
public class RetryContextConfig {
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryContextConfig.class);

    @Bean
    public RetryListener defaultRetryListener(){
        return new RetryListenerSupport(){
            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                LOGGER.debug("closing resources for retry callback held something");
                ExceptionUtilsExt.dump(throwable);
                super.close(context, callback, throwable);
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                LOGGER.debug("第{}次重试未成功，请关注", context.getRetryCount());
                super.onError(context, callback, throwable);
            }

            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                LOGGER.debug("you could put some logic for opening retry operation or not");
                return super.open(context, callback);
            }
        };
    }

}
