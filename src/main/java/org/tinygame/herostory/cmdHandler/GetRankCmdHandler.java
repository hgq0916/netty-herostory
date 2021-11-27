package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.msg.GameMsgProtocol;
import org.tinygame.herostory.msg.GameMsgProtocol.GetRankCmd;
import org.tinygame.herostory.rank.RankItem;
import org.tinygame.herostory.rank.RankService;

/**
 * 获取排名命令处理器
 * @author hugangquan
 * @date 2021/11/27 16:35
 */
public class GetRankCmdHandler implements ICmdHandler<GetRankCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GetRankCmd getRankCmd) {
        if(ctx == null || getRankCmd == null){
            return;
        }

        RankService.getInstance().getRankList((rankItems->{

            GameMsgProtocol.GetRankResult.Builder builder = GameMsgProtocol.GetRankResult.newBuilder();

            if(rankItems != null && !rankItems.isEmpty()){
                for(RankItem rankItem : rankItems){

                    GameMsgProtocol.GetRankResult.RankItem.Builder rankItemBuilder = GameMsgProtocol.GetRankResult.RankItem.newBuilder();
                    rankItemBuilder.setRankId(rankItem.getRankId());
                    rankItemBuilder.setUserId(rankItem.getUserId());
                    rankItemBuilder.setUserName(rankItem.getUsername());
                    rankItemBuilder.setHeroAvatar(rankItem.getHeroAvator());
                    rankItemBuilder.setWin(rankItem.getWin());

                    GameMsgProtocol.GetRankResult.RankItem rankItemResult = rankItemBuilder.build();

                    builder.addRankItem(rankItemResult);
                }
            }

            GameMsgProtocol.GetRankResult getRankResult = builder.build();

            //向客户端返回排名信息
            ctx.writeAndFlush(getRankResult);

            return null;
        }));
    }
}
