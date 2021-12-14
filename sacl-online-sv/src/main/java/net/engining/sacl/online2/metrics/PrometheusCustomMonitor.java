package net.engining.sacl.online2.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.distribution.Histogram;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2021-12-07 10:43
 * @since :
 **/
//@Component
public class PrometheusCustomMonitor {
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(PrometheusCustomMonitor.class);

    /**
     * 累加计数
     * 比如：订单数的累加
     */
    private Counter counter;
    /**
     * 摘要
     * 记录最大值，总数，平均值和累加计数。
     * 比如：订单金额的统计
     */
    private DistributionSummary summary;
    /**
     * 测量值
     * 可变动的数值
     * 比如：进行中的订单数
     */
    private AtomicInteger gauge;
    /**
     * 直方图、柱状图
     * 不同分端的数字
     * 暂不处理
     */
    private Histogram histogram;

    @Autowired
    PrometheusMeterRegistry registry;

    @PostConstruct
    private void init() {
        counter = registry.counter("test_counter", "order", "test-svc");
        summary = registry.summary("test_summary", "amount", "test-svc");
        gauge = registry.gauge("test_gauge",new AtomicInteger(0));


    }

    public Counter getCounter() {
        counter.measure().forEach(measurement -> {
            LOGGER.info("{}", measurement.getValue());
        });
        return counter;
    }

    public DistributionSummary getSummary() {
        return summary;
    }
    public AtomicInteger getGauge() {
        return gauge;
    }

}
