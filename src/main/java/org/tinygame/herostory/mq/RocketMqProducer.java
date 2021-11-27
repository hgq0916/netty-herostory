package org.tinygame.herostory.mq;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mq生产者
 * @author hugangquan
 * @date 2021/11/27 21:42
 */
public final class RocketMqProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqProducer.class);

    private static DefaultMQProducer defaultMQProducer;

    private static RocketMqProducer instance = new RocketMqProducer();

    private RocketMqProducer(){

    }

    public static RocketMqProducer getInstance(){
        return instance;
    }

    public static void init(){

        try {
            //初始化生产者，指定组名称为herostroy
            defaultMQProducer = new DefaultMQProducer("herostory");
            //设置ip和端口
            defaultMQProducer.setNamesrvAddr("192.168.25.128:9876");
            //设置失败重试次数
            defaultMQProducer.setRetryTimesWhenSendFailed(3);
            //启动生产者
            defaultMQProducer.start();
        } catch (Exception ex) {
            LOGGER.error("",ex);
        }

    }

    /**
     * 发送消息
     * @param topic 主题
     * @param msg 消息内容
     */
    public void send(String topic,String msg){
        if(StringUtils.isBlank(topic) || StringUtils.isBlank(msg)){
            return;
        }

        try {
            Message message = new Message();
            message.setTopic(topic);
            message.setBody(msg.getBytes());

            defaultMQProducer.send(message);

            LOGGER.info("发送消息成功：topic{},msg:{}",topic,msg);

        } catch (Exception e) {
            LOGGER.error("发送消息失败：",e);
        }

    }


}
