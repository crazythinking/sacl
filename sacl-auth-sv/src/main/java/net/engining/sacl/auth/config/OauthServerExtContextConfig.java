package net.engining.sacl.auth.config;

import cn.hutool.core.lang.Console;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

/**
 * 认证授权服务，提供用于获取token，解析token相关功能，实现认证、授权功能；
 * OAuth 2 有四种授权模式，外加一种刷新模式：
 * authorization_code：授权码模式;
 * implicit：隐式授权模式;
 * password：资源所有者（即用户）密码模式;
 * client_credentials：客户端凭据（客户端ID以及Key）模式;
 * refresh_token：通过以上授权获得的刷新令牌来获取新的令牌;
 *
 * 具体见 Spring Security 文章目录中的 Spring Cloud OAuth2 5种 Grant Type 介绍；
 *
 * @author Eric Lu
 */
@Configuration
@EnableAuthorizationServer
public class OauthServerExtContextConfig extends AuthorizationServerConfigurerAdapter {

	private static final String CLIENT_FIELDS_FOR_UPDATE = "resource_ids, scopes, "
			+ "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
			+ "refresh_token_validity, additional_information, autoapprove";

	private static final String CLIENT_FIELDS = "client_secret, " + CLIENT_FIELDS_FOR_UPDATE;

	private static final String BASE_FIND_STATEMENT = "select client_id, " + CLIENT_FIELDS
			+ " from OAUTH_CLIENT_DETAILS";

	private static final String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

	private static final String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

	private static final String DEFAULT_INSERT_STATEMENT = "insert into OAUTH_CLIENT_DETAILS (" + CLIENT_FIELDS
			+ ", client_id) values (?,?,?,?,?,?,?,?,?,?,?)";

	private static final String DEFAULT_UPDATE_STATEMENT = "update OAUTH_CLIENT_DETAILS " + "set "
			+ CLIENT_FIELDS_FOR_UPDATE.replaceAll(", ", "=?, ") + "=? where client_id = ?";

	private static final String DEFAULT_UPDATE_SECRET_STATEMENT = "update OAUTH_CLIENT_DETAILS "
			+ "set client_secret = ? where client_id = ?";

	private static final String DEFAULT_DELETE_STATEMENT = "delete from OAUTH_CLIENT_DETAILS where client_id = ?";

	/**
	 * 密码模式授权模式
	 */
	public static final String GRANT_TYPE_PASSWORD = "password";

	/**
	 * 授权码模式：授权码模式使用到了回调地址，是最为复杂的方式，通常网站中经常出现的微博，qq第三方登录，都会采用这个形式
	 */
	public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";

	/**
	 * 并非标准授权模式，只是用于标识刷新Token的动作
	 */
	public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

	/**
	 * 简化授权模式
	 */
	public static final String GRANT_TYPE_IMPLICIT = "implicit";

	/**
	 * 客户端模式
	 */
	public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

	@Autowired
	DataSource dataSource;

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Autowired
	public UserDetailsService profileUserDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenStore redisTokenStore;

//	@Autowired
//	private TokenStore jwtTokenStore;

//	@Autowired
//	private JwtAccessTokenConverter jwtAccessTokenConverter;

	@Autowired
	private CommonTokenEnhancer commonTokenEnhancer;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		//正式用jdbc
		JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
		jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
		jdbcClientDetailsService.setDeleteClientDetailsSql(DEFAULT_DELETE_STATEMENT);
		jdbcClientDetailsService.setFindClientDetailsSql(DEFAULT_FIND_STATEMENT);
		jdbcClientDetailsService.setInsertClientDetailsSql(DEFAULT_INSERT_STATEMENT);
		jdbcClientDetailsService.setSelectClientDetailsSql(DEFAULT_SELECT_STATEMENT);
		jdbcClientDetailsService.setUpdateClientDetailsSql(DEFAULT_UPDATE_STATEMENT);
		jdbcClientDetailsService.setUpdateClientSecretSql(DEFAULT_UPDATE_SECRET_STATEMENT);
		clients.withClientDetails(jdbcClientDetailsService);


//		clients
//	        .inMemory()
//				.withClient("sacl-dubbo-prov-sv")
//				.secret(passwordEncoder.encode("sacl-dubbo-prov-sv-123456"))
//				.authorizedGrantTypes("refresh_token", "authorization_code", "password")
//				//token 的有效期
//				.accessTokenValiditySeconds(3600)
//				//用来限制客户端访问的权限,在换取的 token 的时候会带上 scopes 参数,只有在 scopes 定义内的,才可以正常换取 token;
//				.scopes("all")
//				.and()
//				.withClient("sacl-online-sv")
//				.secret(passwordEncoder.encode("sacl-online-sv-67890"))
//				.authorizedGrantTypes("refresh_token", "authorization_code", "password")
//				.accessTokenValiditySeconds(3600)
//				.scopes("all")
//		;

	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Lists.newArrayList(commonTokenEnhancer));

		//token 方式
		endpoints
				//调用此方法才能支持 password 模式
				.authenticationManager(authenticationManager)
				//设置用户验证服务
				.userDetailsService(profileUserDetailsService)
				//指定 jwt token 的存储方式
//				.tokenStore(jwtTokenStore)
//				.accessTokenConverter(jwtAccessTokenConverter)
				//指定 redis token 的存储方式
				.tokenStore(redisTokenStore)
				.tokenEnhancer(enhancerChain)
		;

	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//这个方法限制客户端访问认证接口的权限
		//允许客户端访问 OAuth2 授权接口，否则请求 token 会返回 401
		//允许表单认证  这段代码在授权码模式下会导致无法根据code　获取token
		security.allowFormAuthenticationForClients();
		//允许已授权用户访问 checkToken 接口: /oauth/check_token
		security.checkTokenAccess("isAuthenticated()");
		//允许已授权用户访问获取 token 接口: /oauth/token
		security.tokenKeyAccess("isAuthenticated()");
	}

	public static void main(String[] args) {
//		SecureRandom secureRandom = new SecureRandom();
//		secureRandom.setSeed(666);
//		new BCryptPasswordEncoder(10, secureRandom);
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		String encodePwd = passwordEncoder.encode("sacl-dubbo-prov-sv-123456");
		Console.log("sacl-dubbo-prov-sv-123456:{}",encodePwd);
		encodePwd = passwordEncoder.encode("sacl-online-sv-67890");
		Console.log("sacl-online-sv-67890:{}",encodePwd);
		encodePwd = passwordEncoder.encode("admin");
		Console.log("admin:{}",encodePwd);
		encodePwd = passwordEncoder.encode("sv@dm1n");
		Console.log("sv@dm1n:{}",encodePwd);

		Console.log(passwordEncoder.matches("sv@dm1n", encodePwd));
		//bas464
		//sacl-online-sv:sacl-online-sv-67890
		//c2FjbC1vbmxpbmUtc3Y6c2FjbC1vbmxpbmUtc3YtNjc4OTA=
		//sacl-dubbo-prov-sv:sacl-dubbo-prov-sv-123456
		//c2FjbC1kdWJiby1wcm92LXN2OnNhY2wtZHViYm8tcHJvdi1zdi0xMjM0NTY=
	}

}
