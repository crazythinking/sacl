package net.engining.template.batch.xxljob.executor.controller;

import com.netflix.loadbalancer.IRule;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import net.engining.pg.batch.sdk.infrastructure.AbstractJobHandler;
import net.engining.pg.support.core.exception.ErrorCode;
import net.engining.pg.support.core.exception.ErrorMessageException;
import org.springframework.stereotype.Service;

import java.net.URL;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-09-25 15:34
 * @since :
 **/
@Service
public class JobHandlerGroovyService {

    private GroovyClassLoader groovyClassLoader;

    public JobHandlerGroovyService() {
        groovyClassLoader = new GroovyClassLoader();
    }

    public DynamicJobHandler loadGroovyJobHandlerFromFile(String filePath)
                                                            throws InstantiationException, IllegalAccessException {
        URL url = this.getClass().getClassLoader().getResource(filePath);
        Class<?> clazz = groovyClassLoader.parseClass(new GroovyCodeSource(url));
        if (clazz != null) {
            Object instance = clazz.newInstance();
            if (instance != null) {
                if (instance instanceof DynamicJobHandler) {
                    return (DynamicJobHandler) instance;
                }
            }
        }
        throw new ErrorMessageException(ErrorCode.CheckError, "读取groovy文件异常");
    }

}
