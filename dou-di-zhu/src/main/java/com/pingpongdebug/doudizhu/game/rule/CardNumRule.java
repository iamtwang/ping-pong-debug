package com.pingpongdebug.doudizhu.game.rule;


import com.pingpongdebug.doudizhu.game.constant.CardConst;
import com.pingpongdebug.doudizhu.game.manager.PlatformManager;
import com.pingpongdebug.doudizhu.game.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 牌数规则
 * 牌数规则 发牌完成校验，平台管理
 * 1.一共54张牌
 * 2.每人17张
 * 3.底牌三张
 */
public class CardNumRule implements Rule {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardNumRule.class);

    /**
     * 检查牌数
     *
     * @param manager 管理平台
     */
    public void checkCardNum(PlatformManager manager) {
        List<String> cardContainer = manager.getAllCards();
        if (cardContainer == null || cardContainer.size() != CardConst.TOTAL_CARDS) {
            LOGGER.error("Error, Must be {} cards", CardConst.TOTAL_CARDS);
            System.exit(1);
        }
        Player[] playerArr = manager.getPlayerArr();
        boolean everyNum = Arrays.stream(playerArr).anyMatch(player -> player.getCardNum() != CardConst.INIT_CARD_NUMBER);
        if (everyNum) {
            LOGGER.error("Error, Every One has {} cards", CardConst.INIT_CARD_NUMBER);
            System.exit(1);
        }
        List<String> bottom = manager.getBottomCards();
        if (bottom == null || bottom.size() != CardConst.BOTTOM_NUM) {
            LOGGER.error("Error, {} cards for DiZhu", CardConst.BOTTOM_NUM);
            System.exit(1);
        }
    }

}
