package org.tinygame.herostory.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis工具
 * @author hugangquan
 * @date 2021/11/27 16:52
 */
public final class RedisUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    private static JedisPool jedisPool;

    private static final RedisUtil instance = new RedisUtil();

    private RedisUtil(){

    }

    public static RedisUtil getInstance(){
        return instance;
    }

    public static void init(){
        try{
            jedisPool = new JedisPool("192.168.25.128",6379);
        }catch (Exception ex){
            LOGGER.error("创建jedisPool出错：",ex);
        }

    }

    public Jedis getRedis(){
        if(jedisPool == null){
            throw new RuntimeException("jedisPool 尚未初始化");
        }

        return jedisPool.getResource();
    }

}
