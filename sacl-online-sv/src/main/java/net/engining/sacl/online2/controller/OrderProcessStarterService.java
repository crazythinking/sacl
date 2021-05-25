package net.engining.sacl.online2.controller;

import com.google.common.collect.Maps;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;
import net.engining.zeebe.spring.client.ext.ZeebeStarterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * @author : Eric Lu
 * @date : 2021-05-13 15:15
 **/
@Service
public class OrderProcessStarterService implements ZeebeStarterHandler<Map<String, Object>> {
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProcessStarterService.class);
    public static final String PROCESS_ID = "order-process";
    public static final String PROCESS_WORKER = "orderProcess-worker";

    @Autowired
    private ZeebeClientLifecycle client;

    /**
     * non-block flow
     */
    public ProcessInstanceEvent startProcess() {
        Map<String, Object> a = Maps.newHashMap();
        a.put("a", UUID.randomUUID());

        ProcessInstanceEvent processInstanceEvent = defaultStart(
                PROCESS_ID,
                a,
                null,
                null,
                null
        ).orElse(null);

        return processInstanceEvent;
    }

    @Override
    public ZeebeClientLifecycle getZeebeClientLifecycle() {
        return client;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public String getTypeId() {
        return PROCESS_ID;
    }

    @Override
    public void before(Map<String, Object> event) {
        //do nothing
    }

    @Override
    public void after(Map<String, Object> event, boolean rt) {
        //do nothing
    }
}
