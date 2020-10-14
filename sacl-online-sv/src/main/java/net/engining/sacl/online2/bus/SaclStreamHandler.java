package net.engining.sacl.online2.bus;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.bus.SpringCloudBusClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-09-24 14:34
 * @since :
 **/
//@EnableBinding(Processor.class)
@EnableBinding(SpringCloudBusClient.class)
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

    public void sentUser(String name){
        User user = new User();
        user.setUserId(System.currentTimeMillis());
        user.setName(name);
        user.setAge(RandomUtil.randomInt(1, 100));
        messageChannel.send(MessageBuilder.createMessage(user, new MessageHeaders(Maps.newHashMap())));
    }

    //@SendTo(Processor.OUTPUT)
    @SendTo(SpringCloudBusClient.OUTPUT)
    public User sentUser2(String name){
        User user = new User();
        user.setUserId(System.currentTimeMillis());
        user.setName(name);
        user.setAge(RandomUtil.randomInt(1, 100));
        return user;
    }



}
