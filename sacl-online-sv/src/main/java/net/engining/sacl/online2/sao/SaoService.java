package net.engining.sacl.online2.sao;

import com.alibaba.fastjson.JSON;
import feign.Request;
import net.engining.sacl.online2.controller.Foo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.stereotype.Service;

import java.util.List;

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
        String response = sao.echo2(
//                new Request.Options(
//                        feignHttpClientProperties.getConnectionTimeout(),
//                        config.getReadTimeout()
//                )
        );

        return JSON.parseArray(response, Foo.class);
    }
}
