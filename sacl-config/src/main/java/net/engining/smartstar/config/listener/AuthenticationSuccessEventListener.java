package net.engining.smartstar.config.listener;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

/**
 * @author Eric Lu
 * @date 2019-12-02 18:46
 **/
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    
    /** logger */
    private static final Logger log = LoggerFactory.getLogger(AuthenticationSuccessEventListener.class);

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        log.debug(JSON.toJSONString(event.getAuthentication()));
    }
}
