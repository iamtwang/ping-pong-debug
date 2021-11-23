package com.pingpongdebug.doudizhu.game.constant;

import com.google.common.collect.ImmutableList;

import java.util.List;


public class CardConst {

    /**
     * All Cards Point, include Joker
     */
    public static final List<String> POINTS_POOL =
            ImmutableList.of("3","4","5","6","7","8","9","10","11","12","13","14","15","16","17");

    /**
     * No Joker
     * 11->J 12->Q 13->K 14>A 15->2
     */
    public static final List<String> CARD_POINTS =
            ImmutableList.of("3","4","5","6","7","8","9","10","11","12","13","14","15");

    /**
     * 16->小王 17->大王
     */
    public static final List<String> CARD_JOKER = ImmutableList.of("16","17");

    /**
     * 纸牌花色
     */
    public static final List<String> CARD_SUITS = ImmutableList.of("♠","♥","♦","♣");

    /**
     * ToTal
     */
    public static final int TOTAL_NUM = 54;

    /**
     * Cards for DiZhu
     */
    public static final int BOTTOM_NUM = 3;

    /**
     * Init cards
     */
    public static final int INIT_CARD_NUMBER = 17;
}
