package org.tinygame.herostory.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.attk.UserAttkMsg;
import org.tinygame.herostory.util.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * rocketmq消费者
 * @author hugangquan
 * @date 2021/11/28 09:38
 */
public final class RocketMqConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqConsumer.class);

    private static DefaultMQPushConsumer defaultMQPushConsumer;

    private static RocketMqConsumer instance = new RocketMqConsumer();


    private RocketMqConsumer(){

    }

    public RocketMqConsumer getInstance(){
        return instance;
    }

    public static void init(){

        try {

            defaultMQPushConsumer = new DefaultMQPushConsumer("herostory");
            defaultMQPushConsumer.setNamesrvAddr("192.168.25.128:9876");

            defaultMQPushConsumer.subscribe("Victor","*");

            defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                    if(msgs == null || msgs.isEmpty()){
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    for(MessageExt messageExt : msgs){
                        byte[] body = messageExt.getBody();

                        if(body == null ||body.length ==0){
                            continue;
                        }

                        String msgBody = new String(body);

                        LOGGER.info("收到Victor消息:"+msgBody);

                        UserAttkMsg userAttkMsg = JSON.parseObject(msgBody, UserAttkMsg.class);

                        Integer attkUserId = userAttkMsg.getAttkUserId();
                        Integer targetId = userAttkMsg.getTargetId();

                        //更新redis中用户的输赢次数
                        try(Jedis jedis = RedisUtil.getInstance().getRedis()){

                            jedis.hincrBy("User:"+attkUserId,"win",1);
                            jedis.hincrBy("User:"+targetId,"lose",1);

                            //更新排行榜中用户的输赢次数
                            String win = jedis.hget("User:" + attkUserId, "win");

                            int winCount = Integer.parseInt(win);

                            jedis.zadd("Rank",winCount,String.valueOf(attkUserId));

                        }

                    }

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

            //启动消费者
            defaultMQPushConsumer.start();

        } catch (Exception e) {
            LOGGER.error("",e);
        }

    }



}
