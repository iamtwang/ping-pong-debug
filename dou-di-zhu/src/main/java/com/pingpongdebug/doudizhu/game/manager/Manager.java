package com.pingpongdebug.doudizhu.game.manager;


import com.pingpongdebug.doudizhu.game.context.PlatformContext;
import com.pingpongdebug.doudizhu.game.player.Player;
import com.pingpongdebug.doudizhu.game.player.PlayerModel;

public interface Manager {

    Manager playerInit(Player... players);

    /**
     * 纸牌初始化
     *
     * @return Manager
     */
    Manager cardInit();

    /**
     * 洗牌
     *
     * @return Manager
     */
    Manager shuffle();

    Manager giveCards();

    /**
     * 结算
     *
     * @param player 当前玩家
     * @return Manager
     */
    Manager settle(Player player);

    /**
     * 注册全局参数
     *
     * @param context 全局参数
     * @return Manager
     */
    Manager register(PlatformContext context);

    /**
     * 根据id获取玩家
     *
     * @param id 玩家id
     * @return 具体玩家
     */
    PlayerModel getPlayer(String id);

    /**
     * 获取随机玩家
     *
     * @return 具体玩家
     */
    PlayerModel getRandomPlayer();

    /**
     * 获取下一个玩家
     *
     * @return 具体玩家
     */
    PlayerModel getNextPlayer();

    /**
     * 获取下一个玩家,只获取一轮
     * @param firstId 从哪个玩家开始算
     * @return 具体玩家
     */
    PlayerModel getNextPlayerRound(String firstId);
}
