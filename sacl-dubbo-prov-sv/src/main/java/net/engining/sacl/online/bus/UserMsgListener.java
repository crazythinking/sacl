package net.engining.sacl.online.bus;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import net.engining.sacl.config.bus.StreamInOutChannel;
import net.engining.sacl.config.bus.StreamPollableInput;
import net.engining.sacl.online.service.Foo;
import net.engining.sacl.online.service.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.bus.SpringCloudBusClient;
import org.springframework.cloud.bus.event.AckRemoteApplicationEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.integration.StaticMessageHeaderAccessor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-09-11 12:22
 * @since :
 **/
@EnableBinding({
        SpringCloudBusClient.class,
        StreamPollableInput.class
})
@Component
public class UserMsgListener {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMsgListener.class);

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(StreamPollableInput.POLLINPUT)
    private PollableMessageSource pollableMessageSource;

    @Scheduled(fixedDelay = 5000)
    public void pollMessage() {
        boolean rt = pollableMessageSource.poll(
                handler -> {
                    UUID id = StaticMessageHeaderAccessor.getId(handler);
                    Foo user2 = (Foo) handler.getPayload();
                    LOGGER.debug("MsgId:{}, body:{}", id, JSON.toJSONString(user2));
                },
                new ParameterizedTypeReference<Foo>() {
                }
        );

        LOGGER.debug("poll message process state: {}", rt);
    }

    @StreamListener(value = SpringCloudBusClient.INPUT, condition = "headers['gender']==1")
    public void receive(@Payload User event) {
        LOGGER.debug(JSON.toJSONString(event));
    }

    @StreamListener(value = SpringCloudBusClient.INPUT, condition = "headers['gender']!=1")
    public void receive2(@Payload User event) {
        throw new ErrorMessageException(ErrorCode.CheckError, "test");
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
