package net.engining.sacl.online.bus;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.engining.sacl.online.service.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.SpringCloudBusClient;
import org.springframework.cloud.bus.event.AckRemoteApplicationEvent;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-09-11 12:22
 * @since :
 **/
@EnableBinding(SpringCloudBusClient.class)
@Component
public class UserMsgListener {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMsgListener.class);

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @StreamListener(SpringCloudBusClient.INPUT)
    public void receive(@Payload GenericRemoteApplicationEvent<User> event){
        LOGGER.debug(JSON.toJSONString(event));
    }

    @EventListener
    public void onEvent(UserRemoteApplicationEvent event) {
        LOGGER.info(
                "Server [port : {}] listeners on {}",
                serverProperties.getPort(),
                event.getUser()
        );
    }

    @EventListener
    public void onEvent4Generic(GenericRemoteApplicationEvent<User> event) throws JsonProcessingException {
        LOGGER.info(
                "Server [port : {}] listeners on {}",
                serverProperties.getPort(),
                objectMapper.writeValueAsString(event.getTarget())
        );
    }

    @EventListener
    public void onAckEvent(AckRemoteApplicationEvent event) throws JsonProcessingException {
        LOGGER.info("Server [port : {}] listeners on {}", serverProperties.getPort(),
                objectMapper.writeValueAsString(event));
    }

}
