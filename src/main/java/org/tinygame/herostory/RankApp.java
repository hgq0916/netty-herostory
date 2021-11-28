package org.tinygame.herostory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.mq.RocketMqConsumer;
import org.tinygame.herostory.util.RedisUtil;

import java.io.IOException;

/**
 * @author hugangquan
 * @date 2021/11/28 09:58
 */
public class RankApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankApp.class);

    public static void main(String[] args) {

        LOGGER.info("启动排行榜进程...");

        //初始化redis连接
        RedisUtil.init();
        //初始化rocket消费者
        RocketMqConsumer.init();

    }

}
