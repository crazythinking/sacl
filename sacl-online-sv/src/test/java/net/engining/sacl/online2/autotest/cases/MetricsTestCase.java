package net.engining.sacl.online2.autotest.cases;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.search.Search;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import net.engining.sacl.online2.autotest.support.AbstractTestCaseTemplate;
import net.engining.sacl.online2.metrics.DbStepMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-10-31 18:25
 * @since :
 **/
@ActiveProfiles(profiles = {
        "metrics"
})
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
public class MetricsTestCase extends AbstractTestCaseTemplate {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    //@Autowired
    //PrometheusMeterRegistry registry;

    @Autowired
    DbStepMeterRegistry dbStepMeterRegistry;

    @Override
    public void initTestData() throws Exception {
        //Metrics.addRegistry(new SimpleMeterRegistry());
        //Metrics.addRegistry(registry);
        dbStepMeterRegistry.config()
                .meterFilter(MeterFilter.denyNameStartsWith("system"))
                .meterFilter(MeterFilter.denyNameStartsWith("jvm"))
                .meterFilter(MeterFilter.denyNameStartsWith("rabbitmq"))
                .meterFilter(MeterFilter.denyNameStartsWith("hikaricp"))
                .meterFilter(MeterFilter.denyNameStartsWith("log4j2"))
                .meterFilter(MeterFilter.denyNameStartsWith("spring"))
                .meterFilter(MeterFilter.denyNameStartsWith("jdbc"))
                .meterFilter(MeterFilter.denyNameStartsWith("process"))

        ;
        Metrics.addRegistry(dbStepMeterRegistry);
    }

    @Override
    public void assertResult() throws Exception {

    }

    @Override
    public void testProcess() throws Exception {
        Order order1;
        for (int i = 0; i < 10; i++) {
            order1 = new Order();
            order1.setOrderId(UUID.randomUUID().toString());
            order1.setAmount(RandomUtil.randomInt(1,500));
            order1.setChannel(RandomUtil.randomEle(Lists.newArrayList("CHANNEL_A", "CHANNEL_B", "CHANNEL_C")));
            order1.setCreateTime(LocalDateTime.now());
            createOrder(order1);
            if(i % 2 == 0) {
                Thread.sleep(500L);
            }
            else {
                Thread.sleep(200L);
            }
        }
        //Search.in(dbStepMeterRegistry)
        // .meters().forEach(each -> {
        //    StringBuilder builder = new StringBuilder();
        //    builder.append("name:")
        //            .append(each.getId().getName())
        //            .append(",tags:")
        //            .append(each.getId().getTags())
        //            .append(",type:").append(each.getId().getType())
        //            .append(",value:").append(each.measure());
        //    Console.log(builder);
        //    //清理
        //    //Metrics.globalRegistry.remove(each);
        //    dbStepMeterRegistry.remove(each);
        //});
        Console.log(Search.in(dbStepMeterRegistry).meters().size());
    }

    @Override
    public void end() throws Exception {

    }

    private void createOrder(Order order) {
        //忽略订单入库等操作
        dbStepMeterRegistry.counter("order.create",
                "channel", order.getChannel(),
                "createTime", FORMATTER.format(order.getCreateTime())
        ).increment();
        //Metrics.counter("order.create",
        //        "channel", order.getChannel(),
        //        "createTime", FORMATTER.format(order.getCreateTime())).increment();

    }

    static class Order {

        private String orderId;
        private Integer amount;
        private String channel;
        private LocalDateTime createTime;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                    .add("orderId='" + orderId + "'")
                    .add("amount=" + amount)
                    .add("channel='" + channel + "'")
                    .add("createTime=" + createTime)
                    .toString();
        }
    }

}
