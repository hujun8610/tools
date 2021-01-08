package org.example;

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
 * @ClassName PddSend.java
 * @Description
 * @createTime 2021-01-07 21:12:00
 */
@Slf4j
@Component
public class PddSend implements CommandLineRunner {

    @Value("${order.main.status.change.rocket.topic}")
    private String topic;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void run(String... args) throws Exception {
        List<String> list = new ArrayList<>();
        list.add("{\"bizType\":0,\"createTime\":1610012833000,\"mobile\":\"18302108639\",\"orderNo\":\"2101076403000101872\",\"orderStatus\":302,\"orderinfoList\":[{\"goodsId\":\"2020999900102128\",\"payWay\":1,\"productCode\":\"100000001128\",\"productName\":\"5G特惠流量包30元5GB（首月0元体验版）\",\"status\":302,\"subOrderNo\":\"2101076403000101872-00\"}],\"pageId\":1338658617768599552,\"pay\":false,\"sourceApp\":\"cbpsp-pkg\",\"sourceOrderNo\":\"1347117566444367872\",\"touchCode\":\"P00000006270\",\"touchName\":\"拼多多存量产品\",\"updateTime\":1610012841443}");
        list.add("{\"bizType\":0,\"createTime\":1610008674000,\"mobile\":\"17262753043\",\"orderNo\":\"2101075987200101536\",\"orderStatus\":302,\"orderinfoList\":[{\"goodsId\":\"2020999900102128\",\"payWay\":1,\"productCode\":\"100000001128\",\"productName\":\"5G特惠流量包30元5GB（首月0元体验版）\",\"status\":302,\"subOrderNo\":\"2101075987200101536-00\"}],\"pageId\":1338658617768599552,\"pay\":false,\"sourceApp\":\"cbpsp-pkg\",\"sourceOrderNo\":\"1347100123633270784\",\"touchCode\":\"P00000006270\",\"touchName\":\"拼多多存量产品\",\"updateTime\":1610008682300}");
        list.add("{\"bizType\":0,\"createTime\":1610003741000,\"mobile\":\"13451788041\",\"orderNo\":\"2101075493900101504\",\"orderStatus\":302,\"orderinfoList\":[{\"goodsId\":\"2020999900102128\",\"payWay\":1,\"productCode\":\"100000001128\",\"productName\":\"5G特惠流量包30元5GB（首月0元体验版）\",\"status\":302,\"subOrderNo\":\"2101075493900101504-00\"}],\"pageId\":1338658617768599552,\"pay\":false,\"sourceApp\":\"cbpsp-pkg\",\"sourceOrderNo\":\"1347079434322821120\",\"touchCode\":\"P00000006270\",\"touchName\":\"拼多多存量产品\",\"updateTime\":1610003745359}");
        list.add("{\"bizType\":0,\"createTime\":1610005616000,\"mobile\":\"19528807143\",\"orderNo\":\"2101075681200101528\",\"orderStatus\":302,\"orderinfoList\":[{\"goodsId\":\"2020999900102128\",\"payWay\":1,\"productCode\":\"100000001128\",\"productName\":\"5G特惠流量包30元5GB（首月0元体验版）\",\"status\":302,\"subOrderNo\":\"2101075681200101528-00\"}],\"pageId\":1338658617768599552,\"pay\":false,\"sourceApp\":\"cbpsp-pkg\",\"sourceOrderNo\":\"1347087290120990720\",\"touchCode\":\"P00000006270\",\"touchName\":\"拼多多存量产品\",\"updateTime\":1610005619244}");

        list.forEach(msg->{
            SendResult result = rocketMQTemplate.syncSend(topic, msg, 30 * 1000);
            log.info("the msgId {} result is [{}]", result.getMsgId(), result.getSendStatus());
        });
    }
}
