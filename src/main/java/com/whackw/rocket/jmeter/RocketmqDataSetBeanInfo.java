package com.whackw.rocket.jmeter;

import com.whackw.rocket.RocketMQConstant;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.FileEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
public class RocketmqDataSetBeanInfo extends BeanInfoSupport {
    private static final Logger log = LoggerFactory.getLogger(RocketmqDataSetBeanInfo.class);
    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRAT_KEY = "secretKey";
    private static final String ENDPOINNTS = "endpoints";
    private static final String TAG = "tag";
    private static final String CONSUMER_GROUP = "consumerGroup";
    private static final String TOPIC = "topic";
    private static final String JMETER_VARIABLE_KEY_NAME = "jmeterVariableKeyName";
    /**
     * Construct a BeanInfo for the given class.
     *
     * @param beanClass class for which to construct a BeanInfo
     */
    static{
        log.info("static  RocketmqDataSetBeanInfo------------------static!!!");
    }
     public RocketmqDataSetBeanInfo() {
        super(RocketmqDataSet.class);
        log.info("RocketmqDataSetBeanInfo!!!");
        createPropertyGroup("rocket_data",             //$NON-NLS-1$
                new String[] { ACCESS_KEY,SECRAT_KEY,ENDPOINNTS,TAG,CONSUMER_GROUP,TOPIC,JMETER_VARIABLE_KEY_NAME});

        PropertyDescriptor p = property(ACCESS_KEY);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, RocketMQConstant.accessKey);
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);
        log.info("RocketmqDataSetBeanInfo!!!-->"+p.getValue(ACCESS_KEY));

        p = property(SECRAT_KEY);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, RocketMQConstant.secretKey);
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);

        p = property(ENDPOINNTS);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, RocketMQConstant.endPoint);
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);

        p = property(TAG);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, RocketMQConstant.tag);
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);

        p = property(CONSUMER_GROUP);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, RocketMQConstant.consumerGroup);
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);


        p = property(TOPIC);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "jmetertest");
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);


        p = property(JMETER_VARIABLE_KEY_NAME);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "MQDATA");

    }
}
