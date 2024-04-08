package com.whackw.rocket;

public class RocketMQConstant {
    public static final String accessKey = "akrgdaazbn9d9fcc6fa948";
    public static final String secretKey = "sk125ee6b2ba9eed2b";
    public static final String endPoint = "rmq-rgdaazbn.rocketmq.sh.public.tencenttdmq.com:8080";
    public static final String consumerGroup = "jmeterGroup";
    public static final String DEFAULT_TOPIC = "jmetertest";
    public static final String tag = "test";
    public static final String jmeterVariableKeyName = "MQDATA";

    public static void printStackTrace() {
        Thread thread = Thread.currentThread();
        StackTraceElement[] stackTraceElements = thread.getStackTrace();

        for (StackTraceElement element : stackTraceElements) {
            System.out.println("类名：" + element.getClassName());
            System.out.println("方法名：" + element.getMethodName());
            System.out.println("文件名：" + element.getFileName());
            System.out.println("行号：" + element.getLineNumber());
            System.out.println("------------------------");
        }
    }
}
