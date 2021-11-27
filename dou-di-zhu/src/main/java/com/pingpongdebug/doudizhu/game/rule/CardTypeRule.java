package com.pingpongdebug.doudizhu.game.rule;


import com.pingpongdebug.doudizhu.game.constant.CardConst;
import com.pingpongdebug.doudizhu.game.enums.CardTypeEnum;
import com.pingpongdebug.doudizhu.game.player.PlayerImpl;
import com.pingpongdebug.doudizhu.game.utils.CardUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 牌型规则
 * 牌型规则 出牌校验
 * 1.可以出单张 1张
 * 2.可以出一对 2张
 * 3.可以出三连张 3张
 * 4.可以出三带一 4张
 * 5.可以出三带二 5张
 * 6.可以出顺子 5-12张
 * 7.可以出连对 6-8-10-12-14-16-18
 * 8.可以出飞机
 * 9.可以出炸弹 4张
 * 10.可以出王炸 2张
 * 牌型相同 单张,一对,三连张,三带一,三带二,顺子，飞机，炸弹，王炸
 * 牌型不同，必须是炸弹
 */
public class CardTypeRule implements Rule {

    /**
     * 检查牌的类型
     *
     * @param cards 打出的牌
     * @return 检查牌的类型
     */
    public CardTypeEnum checkCardType(String cards, PlayerImpl playerModel) {
        if (cards == null) {
            return null;
        }
        //转成可比较点数
        List<String> cardList = CardUtil.convertCards(cards);
        //最多不能超过自己的牌数
        if (cardList.size() > playerModel.getCardList().size()) {
            return null;
        }
        //出的牌是否合规
        if (!cardList.stream().allMatch(this::cardOfPool)) {
            return null;
        }
        //是否是自己的牌
        if (!playerModel.getCardList().containsAll(cardList)) {
            return null;
        }
        //单张
        if (cardList.size() == 1) {
            return CardTypeEnum.SINGLE;
        }
        //两张
        if (cardList.size() == 2) {
            //一对
            if (this.sameCardNum(cardList, 2)) {
                return CardTypeEnum.PAIR;
            }
            //王炸
            if (this.kingBomb(cardList)) {
                return CardTypeEnum.KING_BOMB;
            }
            return null;
        }
        //三张
        if (cardList.size() == 3) {
            if (this.sameCardNum(cardList, 3)) {
                return CardTypeEnum.THREE_ONLY;
            }
            return null;
        }
        //四张
        if (cardList.size() == 4) {
            //三带一
            if (this.sameCardNum(cardList, 3)) {
                return CardTypeEnum.THREE_WITH_ONE;
            }
            //炸弹
            if (this.sameCardNum(cardList, 4)) {
                return CardTypeEnum.BOMB;
            }
            return null;
        }
        //5张
        if (cardList.size() == 5) {
            //三带二
            if (this.sameCardNum(cardList, 3) && this.sameCardNum(cardList, 2)) {
                return CardTypeEnum.THREE_WITH_TWO;
            }
            //顺子
            if (this.shunZi(cardList)) {
                return CardTypeEnum.SHUN_ZI;
            }
            return null;
        }
        // 大于5张
        if (cardList.size() > 5) {
            //是否顺子
            if (this.shunZi(cardList)) {
                return CardTypeEnum.SHUN_ZI;
            }
            //是否连对
            if (this.evenPair(cardList)) {
                return CardTypeEnum.SEQ_PAIR;
            }
            //是否飞机
            if (this.plane(cardList)) {
                return CardTypeEnum.PLANE;
            }
            
            if(this.isFourWithTwo(cardList)){
                return CardTypeEnum.FOUR_WITH_TWO;
            }

            if(this.isFourWithTwoPair(cardList)){
                return CardTypeEnum.FOUR_WITH_TWO_PAIR;
            }
            
        }
        return null;
    }

    private boolean isFourWithTwo(List<String> cardList) {
        if (cardList.size() == 6) {
            Map<String, List<String>> cardMap = cardList.stream().collect(Collectors.groupingBy(String::toString));

            if (cardMap.entrySet().stream().map(e -> e.getValue()).anyMatch(e -> e.size() == 4)) {
                return cardMap.size() == 2 || cardMap.size() == 3;
            }

        }
        return false;
    }

    private boolean isFourWithTwoPair(List<String> cardList) {
        if (cardList.size() == 8) {
            Map<String, List<String>> cardMap = cardList.stream().collect(Collectors.groupingBy(String::toString));

            if (cardMap.entrySet().stream().map(e -> e.getValue()).anyMatch(e -> e.size() == 4)) {

                if (cardMap.size() == 3){
                    return cardMap.entrySet().stream().map(e -> e.getValue()).filter(e -> e.size() == 2).count() == 2;
                }

            }

        }
        return false;
    }

    /**
     * 校验出的牌是否在点数池中
     *
     * @param card 单张牌
     * @return true：满足
     */
    private boolean cardOfPool(String card) {
        if (card != null) {
            return CardConst.POINTS_POOL.contains(card);
        }
        return false;
    }

    /**
     * 是否有n张相同的牌
     *
     * @param cardList 打出的牌集合
     * @param num      相同牌数
     * @return true:是
     */
    private boolean sameCardNum(List<String> cardList, int num) {
        Map<String, List<String>> cardMap = cardList.stream().collect(Collectors.groupingBy(String::toString));
        return cardMap.values().stream().anyMatch(list -> list.size() == num);
    }

    /**
     * 是否王炸
     *
     * @param cardList 打出的牌
     * @return true:王炸
     */
    private boolean kingBomb(List<String> cardList) {
        if (cardList.size() == 2) {
            return 16 + 17 == Integer.parseInt(cardList.get(0)) + Integer.parseInt(cardList.get(1));
        }
        return false;
    }

    /**
     * 是否顺子
     * 差=1，最大点数必须小于15
     *
     * @param cardList 转换后的牌
     * @return true:顺子
     */
    private boolean shunZi(List<String> cardList) {
        List<String> sortCard = CardUtil.sort(cardList);
        //最后一张牌必须小于15
        if (Integer.parseInt(sortCard.get(sortCard.size() - 1)) >= 15) {
            return false;
        }
        boolean shunZi = true;
        for (int i = 0; i < sortCard.size(); i++) {
            if (i < sortCard.size() - 1) {
                if ((Integer.parseInt(sortCard.get(i + 1)) - Integer.parseInt(sortCard.get(i))) != 1) {
                    shunZi = false;
                    break;
                }
            }
        }
        return shunZi;
    }

    /**
     * 连对
     * 1.最少6张
     * 2.最大点数必须小于15
     * 3.去重后是顺子
     * 4.去重后的每张牌必须在原牌中存在两张
     *
     * @param cardList 转换后的牌
     * @return true:连对
     */
    private boolean evenPair(List<String> cardList) {
        if (cardList.size() < 6 || Integer.parseInt(cardList.get(cardList.size() - 1)) >= 15) {
            return false;
        }
        //去重后的牌
        Set<String> cardSet = new HashSet<>(cardList);
        if (!shunZi(new ArrayList<>(cardSet))) {
            return false;
        }
        Map<String, List<String>> cardMap = cardList.stream().collect(Collectors.groupingBy(String::toString));
        for (String next : cardSet) {
            if (!cardMap.containsKey(next)) {
                return false;
            }
            if (cardMap.get(next).size() != 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否飞机
     * 1.三连去重必须是顺子
     * 2. 去掉三连后必须n个单张，或者n个一对
     *
     * @param cardList 转换后的牌
     * @return true:飞机
     */
    private boolean plane(List<String> cardList) {
        Map<String, List<String>> cardMap = cardList.stream().collect(Collectors.groupingBy(String::toString));
        //三连张容器
        List<String> threeList = new ArrayList<>();
        List<String> allThreeList = new ArrayList<>();
        cardMap.forEach((card, list) -> {
            if (list.size() == 3) {
                threeList.add(card);
                allThreeList.addAll(list);
            }
        });
        if (threeList.size() <2) {
            return false;
        }
        //三连是否是顺子
        if (!this.shunZi(threeList)) {
            return false;
        }
        //副本
        List<String> backList = new ArrayList<>(cardList);
        backList.removeAll(allThreeList);

        //剩余是否单张
        if (backList.size() == threeList.size()) {
            return true;
        }
        //剩余是否对子
        if (backList.size() == 2 * threeList.size()) {
            Map<String, List<String>> backMap = backList.stream().collect(Collectors.groupingBy(String::toString));
            return backMap.values().stream().allMatch(back -> back.size() == 2);
        }
        return false;
    }
}
