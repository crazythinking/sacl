package net.engining.sacl.online2.sao;

import feign.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-08-29 16:31
 * @since :
 **/
@FeignClient(value = "echo-service", url = "http://localhost:10102")
public interface Sao {

    @GetMapping(value = "/sentest/echoObj2")
    String echo2();

    @GetMapping(value = "/sentest/echoObj2")
    String echo2(Request.Options options);

}
