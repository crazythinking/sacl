package net.engining.sacl.online2.controller;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class EchoService {
    /** logger */
    private static final Logger log = LoggerFactory.getLogger(EchoService.class);

    public Foo foo1() throws InterruptedException {
        Foo foo = new Foo();
        foo.setF1("eric1");
        foo.setF2(new BigDecimal(111));
        log.info("foo1 will return foo after 1s, @[{}]", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        Thread.sleep(1000L);
        return foo;
    }

    public Foo foo2() throws InterruptedException {
        Foo foo = new Foo();
        foo.setF1("eric2");
        foo.setF2(new BigDecimal(222));
        log.info("foo2 will return foo after 1s, @[{}]", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        Thread.sleep(2000L);
        return foo;
    }
}
