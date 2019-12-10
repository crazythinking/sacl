package net.engining.sacl.auth.endpoint;

import cn.hutool.core.util.CharsetUtil;
import com.google.common.collect.Sets;
import net.engining.pg.support.utils.ValidateUtilExt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 扩展 {@link org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint}；
 * 必须覆盖原框架的CheckTokenEndpoint Bean，否则会被当做普通的controller，进而被BasicAuthenticationFilter处理；
 *
 * @author Eric Lu
 * @date 2019-12-02 20:22
 **/
@FrameworkEndpoint
public class CheckTokenEndpoint {

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(CheckTokenEndpoint.class);

    private AuthorizationServerEndpointsConfigurer endpoints = new AuthorizationServerEndpointsConfigurer();

    private ResourceServerTokenServices resourceServerTokenServices;

    private AccessTokenConverter accessTokenConverter = new CheckTokenAccessTokenConverter();

    private WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private List<AuthorizationServerConfigurer> configurers = Collections.emptyList();

    @PostConstruct
    public void init() {
        for (AuthorizationServerConfigurer configurer : configurers) {
            try {
                configurer.configure(endpoints);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot configure enpdoints", e);
            }
        }
        endpoints.setClientDetailsService(clientDetailsService);

        this.resourceServerTokenServices = getEndpointsConfigurer().getResourceServerTokenServices();
        this.accessTokenConverter = getEndpointsConfigurer().getAccessTokenConverter();
        this.exceptionTranslator = exceptionTranslator();
    }

    private WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator() {
        return getEndpointsConfigurer().getExceptionTranslator();
    }

    private AuthorizationServerEndpointsConfigurer getEndpointsConfigurer() {
        if (!endpoints.isTokenServicesOverride()) {
            try {
                endpoints.tokenServices(endpoints.getDefaultAuthorizationServerTokenServices());
            }
            catch (Exception e) {
                throw new BeanCreationException("Cannot create token services", e);
            }
        }
        return endpoints;
    }

    @RequestMapping(value = "/oauth/check_token")
    @ResponseBody
    public Map<String, ?> checkToken(@RequestParam("token") String value,
                                     @RequestHeader(value="Authorization") String authorizationStr) throws IOException {
        //从header获取clientId
        String[] tokens = extractAndDecodeHeader(authorizationStr);
        assert tokens.length == 2;
        String clientId = tokens[0];

        OAuth2AccessToken token = resourceServerTokenServices.readAccessToken(value);
        if (token == null) {
            throw new InvalidTokenException("Token was not recognised");
        }

        if (token.isExpired()) {
            throw new InvalidTokenException("Token has expired");
        }

        OAuth2Authentication authentication = resourceServerTokenServices.loadAuthentication(token.getValue());

        Map<String, Object> responseMap = (Map<String, Object>) accessTokenConverter.convertAccessToken(token, authentication);
        Set<String> authorities = (Set<String>) responseMap.get(UserAuthenticationConverter.AUTHORITIES);
        Set<String> filteredAuthorities = Sets.newHashSet();
        if (ValidateUtilExt.isNotNullOrEmpty(clientId)){
            filteredAuthorities = authorities.stream()
                    .filter(s -> s.startsWith(clientId))
                    .map(s -> StringUtils.substringAfter(s,clientId+":"))
                    .collect(Collectors.toSet());
            if (ValidateUtilExt.isNotNullOrEmpty(filteredAuthorities)){
                responseMap.replace(UserAuthenticationConverter.AUTHORITIES, filteredAuthorities);
            }
            else {
                log.warn("no need authorities for this client:{}, do nothing", clientId);
            }
        }
        else {
            log.warn("no client_id passed from header, do nothing");
        }


        return responseMap;
    }

    /**
     * Decodes the header into a username and password.
     *
     * @throws BadCredentialsException if the Basic header is not present or is not valid
     * Base64
     */
    private String[] extractAndDecodeHeader(String header)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes(CharsetUtil.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        }
        catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, CharsetUtil.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] { token.substring(0, delim), token.substring(delim + 1) };
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        log.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        // This isn't an oauth resource, so we don't want to send an
        // unauthorized code here. The client has already authenticated
        // successfully with basic auth and should just
        // get back the invalid token error.
        @SuppressWarnings("serial")
        InvalidTokenException e400 = new InvalidTokenException(e.getMessage()) {
            @Override
            public int getHttpErrorCode() {
                return 400;
            }
        };
        return exceptionTranslator.translate(e400);
    }

    static class CheckTokenAccessTokenConverter implements AccessTokenConverter {
        private final AccessTokenConverter accessTokenConverter;

        CheckTokenAccessTokenConverter() {
            this(new DefaultAccessTokenConverter());
        }

        CheckTokenAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
            this.accessTokenConverter = accessTokenConverter;
        }

        @Override
        public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
            Map<String, Object> claims = (Map<String, Object>) this.accessTokenConverter.convertAccessToken(token, authentication);

            // gh-1070
            claims.put("active", true);		// Always true if token exists and not expired

            return claims;
        }

        @Override
        public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
            return this.accessTokenConverter.extractAccessToken(value, map);
        }

        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
            return this.accessTokenConverter.extractAuthentication(map);
        }
    }
}
