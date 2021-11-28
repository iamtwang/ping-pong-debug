package com.pingpongdebug.doudizhu.game.manager;


import com.pingpongdebug.doudizhu.game.constant.CardConst;
import com.pingpongdebug.doudizhu.game.context.ContextHolder;
import com.pingpongdebug.doudizhu.game.context.PlatformContext;
import com.pingpongdebug.doudizhu.game.player.Player;
import com.pingpongdebug.doudizhu.game.player.PlayerImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.pingpongdebug.doudizhu.game.constant.CardConst.TOTAL_CARDS;
import static com.pingpongdebug.doudizhu.game.constant.CardConst.TOTOAL_PLAYER;

public class PlatformManager implements Manager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformManager.class);

    private List<String> allCards;

    private List<String> bottomCards;

    private Player[] playerArr;


    private PlatformManager(){
        allCards = new ArrayList<>();
        bottomCards = new ArrayList<>();
    }

    @Override
    public Manager playerInit(Player... players) {
        playerArr =  players;
        return this;
    }

    /**
     * 初始化纸牌容器
     *
     * @return Manager
     */
    @Override
    public Manager cardInit() {
        allCards.addAll(CardConst.CARD_POINTS);
        allCards.addAll(CardConst.CARD_POINTS);
        allCards.addAll(CardConst.CARD_POINTS);
        allCards.addAll(CardConst.CARD_POINTS);
        allCards.addAll(CardConst.CARD_JOKER);

        assert (allCards.size() == TOTAL_CARDS);

        return this;
    }

    /**
     * 洗牌
     *
     * @return Manager
     */
    @Override
    public Manager shuffle() {
        Collections.shuffle(allCards);
        return this;
    }


    @Override
    public Manager giveCards() {
        LOGGER.info("Give Cards To Players ... ");
        if (allCards.size() != TOTAL_CARDS || getPlayerArr().length != TOTOAL_PLAYER) {
            throw new IllegalStateException("incorrect cards or player");
        }

        int lx = getPlayerArr().length;
        for (int i = 0; i < allCards.size() - CardConst.BOTTOM_NUM; i++) {
            Player player = playerArr[lx++ % playerArr.length];
            player.accept(allCards.get(i));
        }


        //将剩余的牌放入底牌
        bottomCards.addAll(allCards.subList(allCards.size() - CardConst.BOTTOM_NUM, allCards.size()));

        LOGGER.info("Give Cards To Players ... done . ");

        return this;
    }

    /**
     * 结算
     *
     * @param player 当前玩家
     * @return Manager
     */
    @Override
    public Manager settle(Player player) {
        if (player.getCardNum() == 0) {
            LOGGER.info("Player：{} [ {} ]" + " WIN.", player.getId(), player.getMark() );
            ContextHolder.remove();
        }
        return this;
    }

    /**
     * 注册全局容器
     *
     * @param context 全局参数
     * @return Manager
     */
    @Override
    public Manager register(PlatformContext context) {
        ContextHolder.setContext(context);
        return this;
    }

    /**
     * 构建实体
     *
     * @return PlatformManager
     */
    public static PlatformManager build() {
        return new PlatformManager();
    }

    /**
     * 根据id获取玩家
     *
     * @param id 玩家id
     * @return 具体玩家
     */
    @Override
    public PlayerImpl getPlayer(String id) {
        for(Player player: playerArr){
            if (id.equalsIgnoreCase(player.getId())) {
                return (PlayerImpl) player;
            }
        }
        return null;
    }

    /**
     * 获取随机玩家
     *
     * @return 具体玩家
     */
    @Override
    public PlayerImpl getRandomPlayer() {
        return (PlayerImpl) this.playerArr[(new Random().nextInt(playerArr.length))];
    }

    /**
     * 获取下一个玩家
     *
     * @return 下一个具体玩家
     */
    @Override
    public PlayerImpl getNextPlayer() {
        //current id
        String currId = ContextHolder.getContext().getCurrId();
        if (StringUtils.isBlank(currId)) {
            LOGGER.info("!!没有设置当前玩家id,随机获取一个!!");
            return getRandomPlayer();
        }
        //Get next player
        int index = 0;
        for (int i = 0; i < this.playerArr.length - 1; i++) {
            if (this.playerArr[i].getId().equalsIgnoreCase(currId)) {
                if (i == this.playerArr.length - 1) {
                    index = 0;
                } else {
                    index = i +1;
                }
                break;
            }
        }
        return (PlayerImpl) playerArr[index];
    }

    public List<String> getAllCards() {
        return allCards;
    }

    public void setAllCards(List<String> allCards) {
        this.allCards = allCards;
    }

    public List<String> getBottomCards() {
        return bottomCards;
    }

    public void setBottomCards(List<String> bottomCards) {
        this.bottomCards = bottomCards;
    }

    public Player[] getPlayerArr() {
        return playerArr;
    }

    public void setPlayerArr(Player[] playerArr) {
        this.playerArr = playerArr;
    }

}
