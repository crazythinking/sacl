package net.engining.sacl.online2.controller;

import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.pg.support.utils.ExceptionUtilsExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeoutException;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2021-04-09 17:12
 * @since :
 **/
@Service
public class RetryEchoService {
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryEchoService.class);

    /**
     * 重试：最大3次，延迟从1s开始，最大10s，间隔按5被递延
     */
    @Retryable(
            value = {
                    TimeoutException.class
            },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, maxDelay = 10000, multiplier = 5),
            listeners = {
                    "defaultRetryListener",
            }
    )
    public Foo foo4(String key) throws TimeoutException {
        LOGGER.debug(" -------- entrying foo4");
        Foo foo = new Foo();
        foo.setF1("eric1");
        foo.setF2(new BigDecimal(111));
        if ("em".equals(key)){
            throw new ErrorMessageException(ErrorCode.SystemError, "test retry");
        }
        else if ("tm".equals(key)){
            throw new TimeoutException("timeout");
        }
        return foo;
    }

    /**
     * 对上面的重试机制进行兜底
     */
    @Recover
    public Foo recoverFoo4(TimeoutException timeoutException){
        return new Foo();
    }
}
