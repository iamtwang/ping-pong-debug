package com.pingpongdebug.doudizhu.game.manager;



import com.pingpongdebug.doudizhu.game.constant.CardConst;
import com.pingpongdebug.doudizhu.game.context.ContextHolder;
import com.pingpongdebug.doudizhu.game.context.PlatformContext;
import com.pingpongdebug.doudizhu.game.player.Player;
import com.pingpongdebug.doudizhu.game.player.PlayerModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.pingpongdebug.doudizhu.game.constant.CardConst.TOTAL_CARDS;
import static com.pingpongdebug.doudizhu.game.constant.CardConst.TOTOAL_PLAYER;

public class PlatformManager implements Manager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformManager.class);

    private List<String> allCards;

    private List<String> bottomCards;

    private Player[] playerArr;

    private Map<String, Player> playerMap;

    public PlatformManager(){
        allCards = new ArrayList<>();
        bottomCards = new ArrayList<>();
        playerMap = new HashMap<>();
    }

    @Override
    public Manager playerInit(Player... players) {
        playerArr =  players;
        playerMap.putAll(Arrays.stream(players).collect(Collectors.toMap(Player::getId, a -> a)));
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
        //Collections.shuffle(allCards);
        return this;
    }


    @Override
    public Manager giveCards() {
        LOGGER.info("Give Cards To Players ... ");
        if (allCards.size() != TOTAL_CARDS || getPlayerArr().length != TOTOAL_PLAYER) {
            throw new IllegalStateException("incorrect cards or player");
        }

        int lx = getPlayerArr().length;
//            for (int i = 0; i < allCards.size() - CardConst.BOTTOM_NUM; i++) {
//                Player player = players[lx++ % players.length];
//                player.accept(allCards.get(i));
//            }

        getPlayerArr()[0].accept(Arrays.asList("3", "3", "3", "3", "6", "6", "6", "6", "7", "7", "8", "8", "10", "10", "10", "10", "15"));
        getPlayerArr()[1].accept(Arrays.asList("4", "4", "4", "4", "5", "5", "5", "5", "7", "7", "8", "8", "9", "9", "9", "9", "15"));
        getPlayerArr()[2].accept(Arrays.asList("11", "11", "11", "11", "12", "12", "12", "12", "13", "13", "13", "13", "14", "14", "14", "14", "15"));

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
    public PlayerModel getPlayer(String id) {
        return (PlayerModel) this.playerMap.get(id);
    }

    /**
     * 获取随机玩家
     *
     * @return 具体玩家
     */
    @Override
    public PlayerModel getRandomPlayer() {
        return (PlayerModel) this.playerArr[(new Random().nextInt(playerArr.length))];
    }

    /**
     * 获取下一个玩家
     *
     * @return 下一个具体玩家
     */
    @Override
    public PlayerModel getNextPlayer() {
        //当前玩家id
        String currId = ContextHolder.getContext().getCurrId();
        if (StringUtils.isBlank(currId)) {
            LOGGER.info("!!没有设置当前玩家id,随机获取一个!!");
            return getRandomPlayer();
        }
        //下一个玩家id,默认第一个
        String next = playerArr[0].getId();
        for (int i = 0; i < this.playerArr.length - 1; i++) {
            if (this.playerArr[i].getId().equalsIgnoreCase(currId)) {
                if (i == this.playerArr.length - 1) {
                    next = playerArr[0].getId();
                } else {
                    next = playerArr[i + 1].getId();
                }
                break;
            }
        }

//        int index = 0;
//        for (int i = 0; i < this.playerArr.length - 1; i++) {
//            if (this.playerArr[i].getId().equalsIgnoreCase(currId)) {
//                if (i == this.playerArr.length - 1) {
//                    index = 0;
//                } else {
//                    index = i +1;
//                }
//                break;
//            }
//        }
        return getPlayer(next);
    }

    /**
     * 获取下一个玩家，只获取一轮
     *
     * @param firstId 从哪个玩家开始算
     * @return 下一个具体玩家
     */
    @Override
    public PlayerModel getNextPlayerRound(String firstId) {
        for (; ; ) {
            PlayerModel nextPlayer = getNextPlayer();
            if (nextPlayer.getId().equals(firstId)) {
                return null;
            }
            return nextPlayer;
        }
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

    public Map<String, Player> getPlayerMap() {
        return playerMap;
    }

    public void setPlayerMap(Map<String, Player> playerMap) {
        this.playerMap = playerMap;
    }
}
