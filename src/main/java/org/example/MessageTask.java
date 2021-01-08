package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author hujun
 * @version 1.0.0
 * @ClassName MessageTask.java
 * @Description
 * @createTime 2021-01-06 17:20:00
 */
@Slf4j
public class MessageTask implements Runnable {
    private RocketMQTemplate rocketMQTemplate;
    private String topic;
    private CountDownLatch latch;
    private List<OrderStatusReq> reqList;

    public List<OrderStatusReq> getReqList() {
        return reqList;
    }

    public void setReqList(List<OrderStatusReq> reqList) {
        this.reqList = reqList;
    }

    public MessageTask(RocketMQTemplate rocketMQTemplate, String topic, CountDownLatch latch) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.topic = topic;
        this.latch = latch;
    }

    @Override
    public void run() {
        for (int i = 0; i < reqList.size(); i++) {
            try {
                SendResult result = rocketMQTemplate.syncSend(topic, reqList.get(i), 30 * 1000);
                log.info("the send result msgId [{}],msg status [{}],orderNo [{}]", result.getMsgId(), result.getSendStatus(), reqList.get(i).getOrderNo());
                Thread.sleep(100);
            } catch (Exception e) {
                log.error("send message failed orderNo [{}]", reqList.get(i).getOrderNo(), e);
            }
        }
        latch.countDown();
    }
}
