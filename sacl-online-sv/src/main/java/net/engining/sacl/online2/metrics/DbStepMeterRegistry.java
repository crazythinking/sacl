package net.engining.sacl.online2.metrics;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2021-12-07 15:37
 * @since :
 **/
@Component
public class DbStepMeterRegistry extends StepMeterRegistry {
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DbStepMeterRegistry.class);

    //@PersistenceContext
    //private EntityManager em;

    public DbStepMeterRegistry() {
        super(
                new StepRegistryConfig(){

                    @Override
                    public String prefix() {
                        return null;
                    }

                    @Override
                    public String get(String key) {
                        return null;
                    }

                    @Override
                    public Duration step() {
                        //10秒输出一次
                        return Duration.ofSeconds(10);
                    }
                },
                Clock.SYSTEM
        );
    }

    @Override
    protected void publish() {
        getMeters().forEach(meter -> {
            String name = meter.getId().getName();
            List<Tag> tags = meter.getId().getTags();
            Meter.Type type = meter.getId().getType();
            Iterable<Measurement> measurements = meter.measure();
            if (name.startsWith("order.create")){
                LOGGER.info("name:{},tags:{},type:{},value:{}", name, tags, type, measurements);
            }
            this.remove(meter);
        });
    }

    @Override
    protected TimeUnit getBaseTimeUnit() {
        return TimeUnit.SECONDS;
    }
}
