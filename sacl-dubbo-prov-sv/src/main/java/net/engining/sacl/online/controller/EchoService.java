package net.engining.sacl.online.controller;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Eric Lu
 */
@Service
public class EchoService {
    /** logger */
    private static final Logger log = LoggerFactory.getLogger(EchoService.class);

    public EchoController.Foo foo1() throws InterruptedException {
        EchoController.Foo foo = new EchoController.Foo();
        foo.setF1("eric1");
        foo.setF2(new BigDecimal(111));
        log.info("foo1 will return foo after 1s, @[{}]", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        Thread.sleep(1000L);
        return foo;
    }

    public EchoController.Foo foo2() throws InterruptedException {
        EchoController.Foo foo = new EchoController.Foo();
        foo.setF1("eric2");
        foo.setF2(new BigDecimal(222));
        log.info("foo2 will return foo after 1s, @[{}]", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        Thread.sleep(1000L);
        return foo;
    }
}
