package com.whackw.rocket.jmeter;

import com.whackw.rocket.RocketmqCosumer;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.gui.GUIMenuSortOrder;
import org.apache.jmeter.gui.TestElementMetadata;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.util.JMeterStopThreadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import org.apache.jmeter.config.CSVDataSet;


@GUIMenuSortOrder(1)
@TestElementMetadata(labelResource = "displayName")
public class RocketmqDataSet extends ConfigTestElement
        implements TestBean, LoopIterationListener, NoConfigMerge, Interruptible, TestStateListener {

    private static final Logger log = LoggerFactory.getLogger(RocketmqDataSet.class);
    private static final long serialVersionUID = 999L;
    private transient String accessKey;
    private transient String secretKey;
    private transient String endpoints;
    private transient String tag;
    private transient String consumerGroup;
    private transient String topic;
    private transient String jmeterVariableKeyName;

    @Override
    public void setProperty(JMeterProperty property) {
        super.setProperty(property);
    }

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        log.info("iterationStart!!!");
        final JMeterContext context = getThreadContext();
        String mqData = "";
        log.info("iterationStart-----1111!!!");
        JMeterVariables threadVars = context.getVariables();
        log.info("iterationStart-----11112222!!!");
        try {
            mqData = RocketmqCosumer.rocketmqCosumer.dataQueue.take();
            log.info("iterationStart-----1111222233333!!!");
        } catch (InterruptedException e) {
            log.info("MQ no data!!!");
            throw new JMeterStopThreadException("MQ no data!!!！");
        }
        log.info("this.jmeterVariableKeyName:"+this.jmeterVariableKeyName+",iterationStart,rocketMQ'Data:"+mqData);
        threadVars.put(this.jmeterVariableKeyName, mqData);
    }




    public void setAccessKey(String value) {
        this.accessKey = value;
    }
    public String getAccessKey() { return accessKey; }

    public void setSecretKey(String value) {
        this.secretKey = value;
    }
    public String getSecretKey() { return secretKey; }

    public void setEndpoints(String value) {
        this.endpoints = value;
    }
    public String getEndpoints() { return endpoints; }

    public void setTag(String value) {
        this.tag = value;
    }
    public String getTag() { return tag; }

    public void setConsumerGroup(String value) {
        this.consumerGroup = value;
    }
    public String getConsumerGroup() { return consumerGroup; }

    public void setTopic(String value) {
        this.topic = value;
    }
    public String getTopic() { return topic; }
    public void setJmeterVariableKeyName(String value) {
        this.jmeterVariableKeyName = value;
    }
    public String getJmeterVariableKeyName() { return jmeterVariableKeyName; }

    @Override
    public void testStarted() {
        log.info("RocketmqDataSet dataset testStarted");
        if(RocketmqCosumer.rocketmqCosumer==null){
            RocketmqCosumer.rocketmqCosumer = RocketmqCosumer.getInstance(accessKey,secretKey,endpoints,tag,consumerGroup,topic);
        }
    }

    @Override
    public void testStarted(String s) {
        log.info("RocketmqDataSet dataset testStarted: " + s);
    }

    @Override
    public void testEnded() {
        log.info("RocketmqDataSet dataset testEnded");
            if (RocketmqCosumer.rocketmqCosumer != null) {
                try {
                    RocketmqCosumer.rocketmqCosumer.pushConsumer.close();
                } catch (IOException e) {
                    log.info("RocketmqDataSet dataset testEnded,RocketmqCosumer.rocketmqCosumer.pushConsumer.close() error");
                    e.printStackTrace();
                }finally {
                    RocketmqCosumer.rocketmqCosumer.dataQueue.clear();
                    RocketmqCosumer.rocketmqCosumer = null;
                }
            }

    }

    @Override
    public void testEnded(String s) {
        log.info("RocketmqDataSet dataset testEnded" + s);
    }

    @Override
    public boolean interrupt() {
        return true;
    }
}
