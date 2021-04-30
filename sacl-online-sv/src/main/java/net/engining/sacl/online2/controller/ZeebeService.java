package net.engining.sacl.online2.controller;

import cn.hutool.core.lang.Console;
import com.google.common.collect.Maps;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.response.Topology;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import io.zeebe.client.api.response.WorkflowInstanceResult;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobWorker;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import net.engining.pg.support.core.context.ApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2021-04-16 16:40
 * @since :
 **/
@Service
public class ZeebeService {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZeebeService.class);

    @Autowired
    private ZeebeClientLifecycle client;

    /**
     * non-block flow
     */
    public void startProcesses() {
        if (!client.isRunning()) {
            return;
        }

        final WorkflowInstanceEvent event = client
                .newCreateInstanceCommand()
                .bpmnProcessId("order-process")
                .latestVersion()
                .variables("{\"a\": \"" + UUID.randomUUID() + "\"}")
                .send()
                .join();

        LOGGER.info(
                "started instance for workflowKey='{}', bpmnProcessId='{}', version='{}' with workflowInstanceKey='{}'",
                event.getWorkflowKey(),
                event.getBpmnProcessId(),
                event.getVersion(),
                event.getWorkflowInstanceKey()
        );
    }

    @ZeebeWorker(type = "inventory-service", name = "order-process-worker")
    public void handleInventoryService(final JobClient client, final ActivatedJob job) {
        logJob(job);
        client.newCompleteCommand(job.getKey())
                .variables("{\"foo\": 1}")
                .send()
                .join();
    }

    @ZeebeWorker(type = "shipment-service", name = "order-process-worker")
    public void handleShipmentService(final JobClient client, final ActivatedJob job) {
        logJob(job);
        client.newCompleteCommand(job.getKey())
                .variables("{\"foo\": 1}")
                .send()
                .join();
    }

    @ZeebeWorker(type = "payment-service", name = "order-process-worker")
    public void handleOrderProcess(final JobClient client, final ActivatedJob job) {
        logJob(job);
        client.newCompleteCommand(job.getKey())
                .variables("{\"foo\": 1}")
                .send()
                .join();
    }



    /**
     * non-block flow with object variables
     */
    public void startProcesses4Object() {
        if (!client.isRunning()) {
            return;
        }

        Map<String, Object> a = Maps.newHashMap();
        Foo2 foo2 = new Foo2();
        foo2.setUid(UUID.randomUUID().toString());
        foo2.setF1("f1");
        foo2.setF2(new BigDecimal("1000.25"));
        a.put("a", foo2);

        final WorkflowInstanceEvent event = client
                .newCreateInstanceCommand()
                .bpmnProcessId("demoProcess")
                //指定version
                //.version(1)
                .latestVersion()
                .variables(a)
                .send()
                .join();

        LOGGER.info(
                "started instance for workflowKey='{}', bpmnProcessId='{}', version='{}' with workflowInstanceKey='{}'",
                event.getWorkflowKey(),
                event.getBpmnProcessId(),
                event.getVersion(),
                event.getWorkflowInstanceKey()
        );
    }

    /**
     * Block flow with object variables
     */
    public void startBlockProcesses4Object() {
        if (!client.isRunning()) {
            return;
        }

        Map<String, Object> a = Maps.newHashMap();
        Foo2 foo2 = new Foo2();
        foo2.setUid(UUID.randomUUID().toString());
        foo2.setF1("f1");
        foo2.setF2(new BigDecimal("10.25"));
        a.put("a", foo2);

        final WorkflowInstanceResult result = client
                .newCreateInstanceCommand()
                .bpmnProcessId("demoProcess")
                .latestVersion()
                .variables(a)
                .withResult()
                .send()
                .join();

        LOGGER.info(
                "started instance for workflowKey='{}', bpmnProcessId='{}', version='{}' with workflowInstanceKey='{}', variables={}",
                result.getWorkflowKey(),
                result.getBpmnProcessId(),
                result.getVersion(),
                result.getWorkflowInstanceKey(),
                result.getVariables()
        );
    }

    /**
     * 当Event到达时由Zeebe自身调用
     */
    @ZeebeWorker(type = "foo", name = "demoProcess-worker")
    public void handleFooJob(final JobClient client, final ActivatedJob job) {
        logJob(job);
        client.newCompleteCommand(job.getKey())
                //.variables("{\"foo\": 1}")
                .send()
                .join();
    }

    /**
     * 当Event到达时由Zeebe自身调用
     */
    @ZeebeWorker(type = "bar", name = "demoProcess-worker")
    public void handleBarJob(final JobClient client, final ActivatedJob job) {
        //获取流程中的所有变量
        final Map<String, Object> variables = job.getVariablesAsMap();
        variables.forEach((s, o) -> Console.log("key={}, value={}", s, o));

        //logJob(job);
        client.newCompleteCommand(job.getKey())
                .send()
                .join();
    }

    @ZeebeWorker(type = "tas", name = "demoProcess-worker")
    public void handleTasJob(final JobClient client, final ActivatedJob job) {
        //获取流程中的所有变量
        final Map<String, Object> variables = job.getVariablesAsMap();
        variables.forEach((s, o) -> Console.log("key={}, value={}", s, o));

        //logJob(job);
        client.newCompleteCommand(job.getKey())
                .send()
                .join();
    }

    /**
     * 该方法需要被后台线程调起，通常是轮询任务
     */
    public void handleFooJobManually() {
        JobWorker jobWorker = client.newWorker()
                .jobType("foo")
                .handler((jobClient, job) -> {
                    //此值会在worker执行完后回填
                    final Map<String, Object> variables = job.getVariablesAsMap();
                    LOGGER.info("get return variables by key orderId={}", variables.get("orderId"));

                    logJob(job);
                    jobClient.newCompleteCommand(job.getKey()).variables("{\"foo\": 1}").send().join();
                })
                .maxJobsActive(5)
                .timeout(3000)
                .requestTimeout(Duration.ofSeconds(1))
                .name("demoProcess-worker")
                .pollInterval(Duration.ofSeconds(5))
                .fetchVariables("orderId")
                .open();

        //jobWorker.close();
    }

    private static void logJob(final ActivatedJob job) {
        LOGGER.info(
                "complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variables: {}]",
                job.getType(),
                job.getKey(),
                job.getElementId(),
                job.getWorkflowInstanceKey(),
                Instant.ofEpochMilli(job.getDeadline()),
                job.getCustomHeaders(),
                job.getVariables());
    }

    public void getTopology() {
        final Topology topology = client.newTopologyRequest().send().join();

        Console.log("Topology:");
        topology.getBrokers()
                .forEach(
                        b -> {
                            Console.log("    " + b.getAddress());
                            b.getPartitions().forEach(
                                    p -> Console.log("      " + p.getPartitionId() + " - " + p.getRole()));
                        });
    }
}
