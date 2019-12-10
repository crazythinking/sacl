package net.engining.smartstar.config.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

/**
 * @author Eric Lu
 * @date 2019-12-02 18:46
 **/
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        event.getAuthentication().getAuthorities();
    }
}
