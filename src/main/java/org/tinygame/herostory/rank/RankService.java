package org.tinygame.herostory.rank;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.AsyncOperation;
import org.tinygame.herostory.AsyncThreadProcessor;
import org.tinygame.herostory.model.UserEntity;
import org.tinygame.herostory.util.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 排名服务
 * @author hugangquan
 * @date 2021/11/27 16:40
 */
public final class RankService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankService.class);

    private static final RankService instance = new RankService();

    private RankService(){

    }

    public static RankService getInstance(){
        return instance;
    }

    /**
     * 获取排名列表
     * @param callback
     */
    public void getRankList(Function<List<RankItem>,Void> callback){

        GetRankListOperation getRankListOperation = new GetRankListOperation(){
            @Override
            public void doFinish() {
                callback.apply(this.getRankItems());
            }
        };

        AsyncThreadProcessor.getInstance().process(getRankListOperation);

    }

    @Data
    private class GetRankListOperation implements AsyncOperation {

        private List<RankItem> rankItems = null;

        @Override
        public void doAsync() {

            /**
             * User_userid :
             *
             * userInfo:{
             * userId
             * username
             * heroAvator
             * }
             * win
             * lose
             */

            try(Jedis jedis = RedisUtil.getInstance().getRedis()){

                //从redis中获取前10名用户id
                Set<Tuple> rank = jedis.zrevrangeByScoreWithScores(
                        "Rank", "+inf", "-inf", 0, 10);

                if(rank == null || rank.isEmpty()){
                    return;
                }

                List<RankItem> items = new ArrayList<>();

                int i = 0;

                for(Tuple tuple : rank){

                    String userId = tuple.getElement();
                    double score = tuple.getScore();

                    RankItem rankItem = new RankItem();

                    rankItem.setRankId(++i);
                    rankItem.setUserId(Integer.parseInt(userId));
                    rankItem.setWin(Double.valueOf(score).intValue());

                    //根据用户id获取用户信息
                    String userInfo = jedis.hget("User:" + userId, "userInfo");

                    if(StringUtils.isBlank(userInfo)){
                        continue;
                    }

                    UserEntity userEntity = JSON.parseObject(userInfo, UserEntity.class);
                    rankItem.setUsername(userEntity.getUsername());
                    rankItem.setHeroAvator(userEntity.getHeroAvatar());

                    items.add(rankItem);

                }

            //绑定排名列表到当前对象
            rankItems = items;
            }catch (Exception ex){
                LOGGER.error("",ex);
            }

        }
    }

}
