package com.pingpongdebug.doudizhu.domain.utils;

import com.google.common.collect.ImmutableBiMap;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CardUtil {

    private static final ImmutableBiMap<String, String> SUIT_POINT_MAPPING = ImmutableBiMap.<String, String>builder()
            .put("11" ,"J")
            .put("12", "Q")
            .put("13", "K")
            .put("14", "A")
            .put("15", "2")
            .put("16", "小王")
            .put("17", "大王")
            .build();
    private static final String SPACE = " ";

    public static String pointToSuit(String card) {
        return SUIT_POINT_MAPPING.getOrDefault(card, card);
    }

    public static String suitToPoint(String card) {
        return SUIT_POINT_MAPPING.inverse().getOrDefault(card,card);
    }

    public static List<String> cardInSuit(List<String> cards) {
        return cards.stream().map(CardUtil::pointToSuit).collect(Collectors.toList());
    }

    public static List<String> cardInPoint(List<String> cards) {
        return cards.stream().map(CardUtil::suitToPoint).collect(Collectors.toList());
    }

    public static List<String> sort(List<String> cards) {
        List<String> pointList = cardInPoint(cards);
        return pointList.stream().map(Integer::parseInt).sorted().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 纸牌重组展示
     *
     * @param cards 下发的纸牌
     * @return 重组后的
     */
    public static List<String> cardRecombineShow(List<String> cards) {
        return cardInSuit(sort(cards));
    }

    /**
     * 出牌转换成可比较点数
     *
     * @param cards 打出的牌
     * @return 可比较点数
     */
    public static List<String> leaveConvert(String cards) {
        List<String> cardList = Arrays.stream(cards.split(SPACE)).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return cardInPoint(sort(cardList));
    }

}
