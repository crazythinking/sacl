package net.engining.sacl.online2.sao;

import com.alibaba.fastjson.JSON;
import feign.Request;
import net.engining.sacl.online2.controller.Foo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-08-29 16:43
 * @since :
 **/
@Service
public class SaoService {

    @Autowired
    FeignClientProperties feignClientProperties;

    @Autowired
    FeignHttpClientProperties feignHttpClientProperties;

    @Autowired
    Sao sao;

    public List<Foo> echo2() {
        FeignClientProperties.FeignClientConfiguration config = feignClientProperties.getConfig().get("echo-service");
        // 指定特定接口的超时配置
        String response = sao.echo2(
                new Request.Options(
                        config.getConnectTimeout(), TimeUnit.SECONDS,
                        3, TimeUnit.SECONDS,
                        false)
        );

        return JSON.parseArray(response, Foo.class);
    }
}
