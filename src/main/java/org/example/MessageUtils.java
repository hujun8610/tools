package org.example;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author hujun
 * @version 1.0.0
 * @ClassName MessageUtils.java
 * @Description
 * @createTime 2021-01-06 14:31:00
 */
//@Component
@Slf4j
public class MessageUtils implements CommandLineRunner {
    private static final Integer THREAD_NUMBER = 2;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new CustomerThreadFactory("gateway"));

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Value("${order.info.status.change.rocketmq.topic}")
    private String topic;

    @Value("${orderfile}")
    private String inputOrder;

    @Override
    public void run(String... args) throws Exception {
        log.info("begin to send msg");

        File orderFile = new File(inputOrder);
        List<String> orderLines = Files.readLines(orderFile, Charsets.UTF_8);
        List<OrderStatusReq> orderList = orderLines.stream().map(line -> {
            String newline = line.substring(2, line.length() - 1);
            return JSON.parseObject(newline, OrderStatusReq.class);
        }).collect(Collectors.toList());

        List<List<OrderStatusReq>> splitList = Lists.partition(orderList, 5);
        System.out.println(splitList.size());
        for (int i = 0; i < splitList.size(); i++) {
            log.info("the batch is [{}]", i);
            CountDownLatch latch = new CountDownLatch(THREAD_NUMBER);
            for (int j = 0; j < THREAD_NUMBER; j++) {
                MessageTask task = new MessageTask(rocketMQTemplate, topic, latch);
                task.setReqList(splitList.get(i));
                executor.execute(task);
            }
            latch.await();
        }
    }


    static class CustomerThreadFactory implements ThreadFactory {
        private String name;
        private String localDateTime;

        public CustomerThreadFactory(String name) {
            this.name = name;
            DateTimeFormatter inFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            this.localDateTime = now.format(inFormat);
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            String threadName = String.format("%s:%s", name, localDateTime);
            thread.setName(threadName);
            return thread;
        }
    }


}
