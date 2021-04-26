package org.example.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hujun
 * @version 1.0.0
 * @ClassName OrderSyncSend.java
 * @Description
 * @createTime 2021-01-08 18:52:00
 */
@Slf4j
//@Component
public class OrderSyncSend implements CommandLineRunner {
    @Value("${order.ordersync.async.order.status.topic}")
    private String topic;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void run(String... args) throws Exception {
        List<String> list = new ArrayList<>();
        list.add("{\"sysOrderId\":\"1347440860703506432\",\"orderNo\":null,\"orderType\":3,\"sourceOrderNo\":\"1347440860507873280\",\"sourceSubOrderNo\":\"1347440860507873280\",\"source\":2,\"asyncOrderStatus\":20,\"errorCode\":\"0110022999\",\"errorMsg\":\"pkg下单接口失败，订购提交前置校验失败，业务资格校验|省份|240|辽宁|城市|240|沈阳|触点|P00000006270失败，该用户不能办理该业务\",\"touchCode\":\"P00000006270\"}");

        list.forEach(msg->{
            SendResult result = rocketMQTemplate.syncSend(topic, msg, 30 * 1000);
            log.info("the msgId {} result is [{}]", result.getMsgId(), result.getSendStatus());
        });
    }
}
