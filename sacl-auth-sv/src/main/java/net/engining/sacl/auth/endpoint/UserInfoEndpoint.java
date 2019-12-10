package net.engining.sacl.auth.endpoint;

import net.engining.pg.web.CommonWithHeaderResponseBuilder;
import net.engining.pg.web.bean.CommonWithHeaderResponse;
import net.engining.profile.sdk.service.ClientWebUser;
import net.engining.profile.sdk.service.ProfileRuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eric Lu
 * @date 2019-11-29 13:01
 **/
@RestController
public class UserInfoEndpoint {

    @Autowired
    ProfileRuntimeService profileRuntimeService;

    @RequestMapping(value = "/oauth/user_info")
    @ResponseBody
    public CommonWithHeaderResponse<Void, ClientWebUser> loginedUserInfo(){

        ClientWebUser clientWebUser = profileRuntimeService.loadCurrentUser();
        return new CommonWithHeaderResponseBuilder<Void, ClientWebUser>().build().setResponseData(clientWebUser);
    }
}
