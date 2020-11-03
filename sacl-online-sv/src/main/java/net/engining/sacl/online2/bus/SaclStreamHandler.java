package net.engining.sacl.online2.bus;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import net.engining.sacl.online2.controller.Foo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.bus.SpringCloudBusClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-09-24 14:34
 * @since :
 **/
@EnableBinding({
        SpringCloudBusClient.class,
        Processor.class
})
@Component
public class SaclStreamHandler {

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(SaclStreamHandler.class);

    @Autowired
    @Qualifier(SpringCloudBusClient.OUTPUT)
    MessageChannel messageChannel;

    @Autowired
    @Qualifier(SpringCloudBusClient.INPUT)
    SubscribableChannel subscribableChannel;

    @Autowired
    @Qualifier(Processor.OUTPUT)
    MessageChannel messageChannel1;

    public void sentUser(String name){
        User user = new User();
        user.setUserId(System.currentTimeMillis());
        user.setName(name);
        user.setAge(RandomUtil.randomInt(1, 100));
        Map<String, Object> headerMap = Maps.newHashMap();
        headerMap.put("gender", user.getAge() % 2);
        messageChannel.send(MessageBuilder.createMessage(user, new MessageHeaders(headerMap)));
    }

    public void sentFoo(String f1){
        Foo foo = new Foo();
        foo.setF1(f1);
        foo.setF2(BigDecimal.valueOf(RandomUtil.randomLong()));
        Map<String, Object> headerMap = Maps.newHashMap();
        messageChannel1.send(MessageBuilder.createMessage(foo, new MessageHeaders(headerMap)));
    }

    @StreamListener(Processor.INPUT)
    public void received(String msg){
        log.debug(msg);
    }

}
