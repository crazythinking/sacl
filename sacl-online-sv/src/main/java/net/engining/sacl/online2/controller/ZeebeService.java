package net.engining.sacl.online2.controller;

import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobWorker;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void startProcesses() {
        if (!client.isRunning()) {
            return;
        }

        final WorkflowInstanceEvent event = client
                .newCreateInstanceCommand()
                .bpmnProcessId("demoProcess")
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

    /**
     * 当Event到达时由Zeebe自身调用
     */
    @ZeebeWorker(type = "foo", name = "demoProcess-worker")
    public void handleFooJob(final JobClient client, final ActivatedJob job) {
        logJob(job);
        client.newCompleteCommand(job.getKey()).variables("{\"foo\": 1}").send().join();
    }

    /**
     * 当Event到达时由Zeebe自身调用
     */
    @ZeebeWorker(type = "bar", name = "demoProcess-worker")
    public void handleBarJob(final JobClient client, final ActivatedJob job) {
        logJob(job);
        client.newCompleteCommand(job.getKey()).send().join();
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
}
