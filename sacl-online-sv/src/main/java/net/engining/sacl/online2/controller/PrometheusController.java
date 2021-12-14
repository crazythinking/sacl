package net.engining.sacl.online2.controller;

import net.engining.sacl.online2.metrics.PrometheusCustomMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2021-12-07 10:46
 * @since :
 **/

@RestController
@RequestMapping("/")
public class PrometheusController {

    @Autowired
    private PrometheusCustomMonitor monitor;

    @RequestMapping("/order")
    public String order(int amount) {
        // 订单总数累加
        monitor.getCounter().increment();
        // 最新订单金额
        monitor.getGauge().set(amount);
        // 增加的订单金额
        monitor.getSummary().record(amount);
        return "下单成功";
    }
}
