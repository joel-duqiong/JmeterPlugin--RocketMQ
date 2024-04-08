package com.whackw.rocket;

import org.apache.rocketmq.client.apis.*;
import org.apache.rocketmq.client.apis.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;

public class RocketmqCosumer {
    private static final Logger log = LoggerFactory.getLogger(RocketmqCosumer.class);
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String consumerGroup;
    private String topic;
    private String tag;
    public static volatile RocketmqCosumer rocketmqCosumer;
    public PushConsumer pushConsumer;
    public final ArrayBlockingQueue<String> dataQueue = new ArrayBlockingQueue<>(100);
    private Thread pollThread;

    public static RocketmqCosumer getInstance(String accessKey, String secretKey, String endpoints, String tag, String consumerGroup, String topic) {
        if (rocketmqCosumer == null) {//第一次
            log.info("getInstance!!!");
            synchronized (RocketmqCosumer.class) {
                if (rocketmqCosumer == null) {
                    log.info("getInstance1!!!");
                    rocketmqCosumer = new RocketmqCosumer(accessKey, secretKey, endpoints, tag, consumerGroup, topic);
                }
            }
        }
        //第二次，并且与之前参数不一致
        if (!rocketmqCosumer.accessKey.equals(accessKey) || !rocketmqCosumer.secretKey.equals(secretKey)
                || !rocketmqCosumer.endPoint.equals(endpoints) || !rocketmqCosumer.tag.equals(tag)
                || !rocketmqCosumer.consumerGroup.equals(consumerGroup) || !rocketmqCosumer.topic.equals(topic)) {
            log.info("getInstance2!!!");
            synchronized (RocketmqCosumer.class) {
                if (!rocketmqCosumer.accessKey.equals(accessKey) || !rocketmqCosumer.secretKey.equals(secretKey)
                        || !rocketmqCosumer.endPoint.equals(endpoints) || !rocketmqCosumer.tag.equals(tag)
                        || !rocketmqCosumer.consumerGroup.equals(consumerGroup) || !rocketmqCosumer.topic.equals(topic)) {
                    log.info("getInstance3!!!");
                    if (rocketmqCosumer != null) {
                        rocketmqCosumer.shutdown();
                    }
                    log.info("getInstance4!!!");
                    rocketmqCosumer = new RocketmqCosumer(accessKey, secretKey, endpoints, tag, consumerGroup, topic);
                }
            }
        }
        log.info("getInstance5!!!");
        return rocketmqCosumer;
    }

    public RocketmqCosumer(String accessKey, String secretKey, String endpoints, String tag, String consumerGroup, String topic) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.endPoint = endpoints;
        this.tag = tag;
        this.consumerGroup = consumerGroup;
        this.topic = topic;

        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        // 添加配置的 ak 和 sk

        SessionCredentialsProvider sessionCredentialsProvider =
                new StaticSessionCredentialsProvider(accessKey, secretKey);

        // 填写腾讯云提供的接入地址

        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(endpoints)
                .enableSsl(false)
                .setCredentialProvider(sessionCredentialsProvider)
                .build();

        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);

        // 通常在一个客户端内无需创建过多的消费者。
        try {

            this.pushConsumer = provider.newPushConsumerBuilder()
                    .setClientConfiguration(clientConfiguration)
                    // Set the consumer group name.
                    .setConsumerGroup(consumerGroup)
                    // Set the subscription for the consumer.
                    .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                    .setMessageListener(messageView -> {
                        // Handle the received message and return consume result.
                        dataQueue.add("MessageId="+messageView.getMessageId().toString()+"&MQbody="+ StandardCharsets.UTF_8.decode(messageView.getBody()).toString());
                        log.info("Consume message={},body={}", messageView,StandardCharsets.UTF_8.decode(messageView.getBody()).toString());
                        return ConsumeResult.SUCCESS;
                    })
                    .build();
        } catch (ClientException e) {
            rocketmqCosumer = null;
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            this.pushConsumer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            rocketmqCosumer = null;
            dataQueue.clear();
        }
    }

}
