package net.engining.sacl.online2.controller;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import net.engining.sacl.online2.bus.GenericRemoteApplicationEvent;
import net.engining.sacl.online2.bus.User;
import net.engining.sacl.online2.bus.UserRemoteApplicationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.event.AckRemoteApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2019-12-26 10:31
 * @since :
 **/
@RestController
@RequestMapping(value={"/bus"})
public class Echo3Controller {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(Echo3Controller.class);

    @Autowired
    private BusProperties busProperties;

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationEventPublisher publisher;

    @ApiOperation(value = "test for bus", notes = "")
    @PostMapping("/publish/user")
    public boolean publish(@RequestParam String name,
                           @RequestParam(required = false) String destination) {
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(name);
        user.setAge(RandomUtil.randomInt(1, 100));
        publisher.publishEvent(
                new UserRemoteApplicationEvent(this, user,  busProperties.getId(), destination)
        );
        return true;
    }

    @ApiOperation(value = "test for bus, generic message", notes = "")
    @PostMapping("/publish/user2")
    public boolean publish2(@RequestParam String name,
                           @RequestParam(required = false) String destination) {
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(name);
        user.setAge(RandomUtil.randomInt(1, 100));
        publisher.publishEvent(
                new GenericRemoteApplicationEvent<User>(this, user, busProperties.getId(), destination)
        );
        return true;
    }

    @EventListener
    public void onAckEvent(AckRemoteApplicationEvent event)
            throws JsonProcessingException {
        LOGGER.info("Server [port : {}] listeners on {}", serverProperties.getPort(),
                objectMapper.writeValueAsString(event));
    }

}
