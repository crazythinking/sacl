package net.engining.sacl.auth.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Eric Lu
 * @date 2019-11-29 18:58
 **/
@Component
public class SimpleCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        httpServletRequest.setCharacterEncoding("utf-8");
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json");
        //允许所有域名访问
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        //允许的访问方式
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type,Authorization");
        httpServletResponse.setHeader("Access-Control-Request-Headers", "x-requested-with,content-type,Accept,Authorization");
        httpServletResponse.setHeader("Access-Control-Request-Method", "GET,POST,PUT,DELETE,OPTIONS");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
