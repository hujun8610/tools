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
 * @ClassName PddSend.java
 * @Description
 * @createTime 2021-01-07 21:12:00
 */
@Slf4j
//@Component
public class PddSend implements CommandLineRunner {

    @Value("${order.main.status.change.rocket.topic}")
    private String topic;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void run(String... args) throws Exception {
        List<String> list = new ArrayList<>();
        list.add("{\"bizType\":10,\"createTime\":1610088478000,\"mobile\":\"15039693812\",\"orderNo\":\"2101085327600111880\",\"orderStatus\":301,\"orderinfoList\":[{\"activityId\":1343806305121144832,\"goodsId\":\"2020999900045764\",\"payWay\":1,\"productCode\":\"500000050015\",\"productName\":\"芝麻卡29-12个月轻合约套卡\",\"status\":301,\"subOrderNo\":\"2101085327600111880-00\"}],\"pageId\":1343871321162555392,\"pay\":false,\"sourceApp\":\"cbpsp-ordersync\",\"sourceOrderNo\":\"file-2074581\",\"touchCode\":\"P00000006212\",\"touchName\":\" 拼多多春节活动芝麻卡10元优惠券\",\"updateTime\":1610088479322,\"uniChannelId\":\"1000000002230100306\"}");
        list.add("{\"bizType\":0,\"createTime\":1610087053000,\"mobile\":\"19526125046\",\"orderNo\":\"2101085185200101560\",\"orderStatus\":302,\"orderinfoList\":[{\"goodsId\":\"2020999900102129\",\"payWay\":1,\"productCode\":\"100000001129\",\"productName\":\"5G特惠流量包30元10GB（首月0元体验版）\",\"status\":302,\"subOrderNo\":\"2101085185200101560-00\"}],\"pageId\":1338658617768599552,\"pay\":false,\"sourceApp\":\"cbpsp-pkg\",\"sourceOrderNo\":\"1347428873848299520\",\"touchCode\":\"P00000006270\",\"touchName\":\"拼多多存量产品\",\"channelSeqId\":\"S2897653863445135362346\",\"uniChannelId\":\"1000000002230100306\",\"updateTime\":1610087058821}");

        list.forEach(msg->{
            SendResult result = rocketMQTemplate.syncSend(topic, msg, 30 * 1000);
            log.info("the msgId {} result is [{}]", result.getMsgId(), result.getSendStatus());
        });
    }
}
