package net.engining.sacl.online2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2019-12-26 10:31
 * @since :
 **/
//@RestController
//@RequestMapping(value={"/bus"})
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

//    @Autowired
//    private SaclStreamHandler saclStreamHandler;

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * consumer端的ServiceId
     */
    private String destination = "sacl-dubbo-prov-sv";

//    @ApiOperation(value = "test for bus", notes = "")
//    @PostMapping("/publish/user")
//    public boolean publish(@RequestParam String name) {
//        User user = new User();
//        user.setUserId(System.currentTimeMillis());
//        user.setName(name);
//        user.setAge(RandomUtil.randomInt(1, 100));
//        publisher.publishEvent(
//                new UserRemoteApplicationEvent(this, user,  busProperties.getId(), destination)
//        );
//        return true;
//    }

//    @ApiOperation(value = "Foo test for stream", notes = "")
//    @PostMapping("/publish/foo")
//    public boolean publish4(@RequestParam String f1) {
//        saclStreamHandler.sentFoo(f1);
//        return true;
//    }

//    @EventListener
//    public void onAckEvent(AckRemoteApplicationEvent event)
//            throws JsonProcessingException {
//        LOGGER.info(
//                "Server [port : {}] listeners on {}", serverProperties.getPort(),
//                objectMapper.writeValueAsString(event));
//    }

    @RequestMapping(value="/{target}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void send(@RequestBody String body, @PathVariable("target") String target){
        resolver.resolveDestination(target).send(new GenericMessage<>(body));
    }

}
