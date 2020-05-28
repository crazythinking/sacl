package net.engining.sacl.auth.config;

import com.google.common.collect.Maps;
import net.engining.profile.security.service.ProfileUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Map;

/**
 * @author Eric Lu
 * @create 2019-11-21 15:58
 **/
public class CommonTokenEnhancer implements TokenEnhancer {

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(CommonTokenEnhancer.class);

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String grantType = authentication.getOAuth2Request().getGrantType();
        Map<String, Object> additionalInfoMap = Maps.newHashMapWithExpectedSize(16);

        //只有如下两种模式才能获取到当前用户信息
        if(OauthServerExtContextConfig.GRANT_TYPE_AUTHORIZATION_CODE.equals(grantType)
                || OauthServerExtContextConfig.GRANT_TYPE_PASSWORD.equals(grantType)) {

            if (authentication.getPrincipal() instanceof ProfileUserDetails){
                ProfileUserDetails profileUserDetails = (ProfileUserDetails) authentication.getPrincipal();

                additionalInfoMap.put("puId", profileUserDetails.getPuId());
                additionalInfoMap.put("userName", profileUserDetails.getUsername());
            }
            else {
                additionalInfoMap.put("userName", authentication.getPrincipal());
            }

        }

        if (accessToken instanceof DefaultOAuth2AccessToken) {
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfoMap);
        }
        else {
            log.warn("accessToken is not an instance of DefaultOAuth2AccessToken, can not set additional information!");
        }

        return accessToken;
    }
}
