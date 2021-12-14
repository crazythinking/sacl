package net.engining.sacl.online2.controller;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import net.engining.sacl.online2.bus.User;
import net.engining.sacl.online2.bus.UserOutputStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@Profile("stream.common")
@RestController
@RequestMapping(value={"/bus"})
public class Echo3Controller {

    //@Autowired
    //private BusProperties busProperties;
    //
    //@Autowired
    //private ServerProperties serverProperties;
    //
    //@Autowired
    //private ObjectMapper objectMapper;
    //
    //@Autowired
    //private ApplicationEventPublisher publisher;

    @Autowired
    private UserOutputStreamHandler userOutputStreamHandler;

    //@Autowired
    //private BinderAwareChannelResolver resolver;

    ///**
    // * consumer端的ServiceId
    // */
    //private String destination = "sacl-dubbo-prov-sv";

    //@ApiOperation(value = "test for bus", notes = "")
    //@PostMapping("/publish/user")
    //public boolean publish(@RequestParam String name) {
    //    User user = new User();
    //    user.setUserId(System.currentTimeMillis());
    //    user.setName(name);
    //    user.setAge(RandomUtil.randomInt(1, 100));
    //    publisher.publishEvent(
    //            new UserRemoteApplicationEvent(this, user,  busProperties.getId(), destination)
    //    );
    //    return true;
    //}

    //@ApiOperation(value = "test for bus, generic message", notes = "")
    //@PostMapping("/publish/user2")
    //public boolean publish2(@RequestParam String name) {
    //    User user = new User();
    //    user.setUserId(System.currentTimeMillis());
    //    user.setName(name);
    //    user.setAge(RandomUtil.randomInt(1, 100));
    //    publisher.publishEvent(
    //            new GenericRemoteApplicationEvent<User>(this, user, busProperties.getId(), destination)
    //    );
    //    return true;
    //}

    @ApiOperation(value = "test for stream", notes = "")
    @PostMapping("/publish/user3")
    public boolean publish3(@RequestParam String name) {
            User user = new User();
            user.setUserId(System.currentTimeMillis());
            user.setName(name);
            user.setAge(RandomUtil.randomInt(1, 100));
        userOutputStreamHandler.send(user, Maps.newHashMap());
        return true;
    }

    //@ApiOperation(value = "Foo test for stream", notes = "")
    //@PostMapping("/publish/foo")
    //public boolean publish4(@RequestParam String f1) {
    //    saclStreamHandler.sentFoo(f1);
    //    return true;
    //}

    //@EventListener
    //public void onAckEvent(AckRemoteApplicationEvent event)
    //        throws JsonProcessingException {
    //    LOGGER.info(
    //            "Server [port : {}] listeners on {}", serverProperties.getPort(),
    //            objectMapper.writeValueAsString(event));
    //}

    //@RequestMapping(value="/{target}")
    //@ResponseStatus(HttpStatus.ACCEPTED)
    //public void send(@RequestBody String body, @PathVariable("target") String target){
    //    resolver.resolveDestination(target).send(new GenericMessage<>(body));
    //}

}
