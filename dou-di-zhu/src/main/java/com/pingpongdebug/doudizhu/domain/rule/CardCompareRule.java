package com.pingpongdebug.doudizhu.domain.rule;

import com.pingpongdebug.doudizhu.domain.context.PlatformContext;
import com.pingpongdebug.doudizhu.domain.enums.CardTypeEnum;
import com.pingpongdebug.doudizhu.domain.utils.CardUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pingpongdebug.doudizhu.domain.enums.CardTypeEnum.*;

/**
 * @description: 大小规则 出票校验，玩家模型
 * * 单张：比大小
 * * 一对：比单张大小
 * * 三带一：比三连的单张大小
 * * 三带二：比三连的单张大小
 * * 顺子：牌数相等，并且比较起始点大小
 * * 飞机：牌数相等，比较飞机起始点
 * * 炸弹：比单张大小
 * * 王炸>4张炸弹>其他
 */
public class CardCompareRule implements Rule {

    /**
     * 出票大小比较
     *
     * @param context  容器
     * @param typeEnum 打出的牌型
     * @param cards    打出的牌
     * @return true:当前牌大
     */
    public boolean compare(PlatformContext context, CardTypeEnum typeEnum, String cards) {
        String preGiveCards = context.getPreGiveCards();
        //转成可比较点数
        List<String> cardList = CardUtil.leaveConvert(cards);
        List<String> preList = CardUtil.leaveConvert(preGiveCards);
        if (typeEnum == KING_BOMB) {
            return true;
        }
        if (context.getPreType() == SINGLE
                || context.getPreType() == PAIR
                || context.getPreType() == THREE_ONLY
                || context.getPreType() == THREE_WITH_ONE
                || context.getPreType() == THREE_WITH_TWO
                || context.getPreType() == PLANE
                || context.getPreType() == FOUR_WITH_TWO
                || context.getPreType() == FOUR_WITH_TWO_PAIR) {
            if (typeEnum == CardTypeEnum.BOMB) {
                return true;
            }
            return compareOne(context.getPreType(), typeEnum, getSameCard(preList), getSameCard(cardList)) && preList.size() == cardList.size();
        }
        if (context.getPreType() == SHUN_ZI || context.getPreType() == SEQ_PAIR) {
            if (typeEnum == CardTypeEnum.BOMB) {
                return true;
            }
            return compareOne(context.getPreType(), typeEnum, preList.get(0), cardList.get(0)) && preList.size() == cardList.size();
        }
        if (context.getPreType() == BOMB) {
            return compareOne(context.getPreType(), typeEnum, preList.get(0), cardList.get(0));
        }
        return false;
    }

    /**
     * 获取相同牌数多的单张牌，比如一对获取单张，三带一获取三个中的单张，三带二获取三个中的单张
     *
     * @param cardList 牌集合
     * @return 相同牌数多的单张牌
     */
    private String getSameCard(List<String> cardList) {
        Map<String, List<String>> cardMap = cardList.stream().collect(Collectors.groupingBy(String::toString));
        int size = 1;
        String currCard = null;
        for (Map.Entry<String, List<String>> entry : cardMap.entrySet()) {
            String card = entry.getKey();
            List<String> list = entry.getValue();
            if (list.size() >= size) {
                size = list.size();
                currCard = card;
            }
        }
        return cardMap.get(currCard).get(0);
    }

    /**
     * 比较单张
     *
     * @param preType  前玩家牌型
     * @param currType 当前玩家牌型
     * @param preCard  前玩家出的牌
     * @param currCard 当前玩家出的牌
     * @return true: 当前玩家大
     */
    private boolean compareOne(CardTypeEnum preType, CardTypeEnum currType, String preCard, String currCard) {
        return preType == currType && Integer.parseInt(currCard) > Integer.parseInt(preCard);
    }

}
