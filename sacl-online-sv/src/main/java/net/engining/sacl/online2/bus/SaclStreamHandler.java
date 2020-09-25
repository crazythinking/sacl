package net.engining.sacl.online2.bus;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.GenericMessage;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-09-24 14:34
 * @since :
 **/
public class SaclStreamHandler {

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(SaclStreamHandler.class);

    @Autowired
    MessageChannel messageChannel;

    @Autowired
    SubscribableChannel subscribableChannel;


    @SendTo(Processor.OUTPUT)
    public User sentUser(String name){
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(name);
        user.setAge(RandomUtil.randomInt(1, 100));
        return user;
    }

    @StreamListener(target = Processor.INPUT)
    public void receive(@Payload User user){
        log.debug(JSON.toJSONString(user));
    }

    public void sentUser2(String name){
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(name);
        user.setAge(RandomUtil.randomInt(1, 100));
        messageChannel.send(new GenericMessage<User>(user));
    }

}
