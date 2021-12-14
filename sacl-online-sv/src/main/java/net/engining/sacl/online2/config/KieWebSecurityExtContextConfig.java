package net.engining.sacl.online2.config;

/**
 * Kie server web security: LOWEST_PRECEDENCE-5
 *
 * @author : Eric Lu
 * @version :
 * @date : 2021-11-11 15:02
 * @since :
 **/
//@Configuration("kieServerSecurity")
//@EnableWebSecurity
//@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class KieWebSecurityExtContextConfig
        //extends WebSecurityConfigurerAdapter
{

    //@Autowired
    //private PasswordEncoder passwordEncoder;
    //
    //@Autowired
    //private KieServerProperties kieServerProperties;
    //
    //@Override
    //protected void configure(HttpSecurity http) throws Exception {
    //    http
    //            .antMatcher("/kie-rest/**")
    //            .authorizeRequests()
    //            .anyRequest().authenticated()
    //            .and()
    //            .httpBasic()
    //            .and()
    //            //针对shutdwon需要关闭跨站请求伪造保护功能，原因不明
    //            .csrf().disable()
    //    ;
    //}
    //
    //@Override
    //public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //    // 使用自定义身份验证组件
    //    auth.inMemoryAuthentication()
    //            .withUser("kieserver")
    //                .password(passwordEncoder.encode("kieserver1!"))
    //                .roles("kie-server","rest-all")
    //            .and()
    //            .withUser("john")
    //                .password(passwordEncoder.encode("john@pwd1"))
    //                .roles("kie-server", "rest-all", "PM", "HR");
    //    auth.eraseCredentials(true);
    //}

}
