package com.pingpongdebug.doudizhu.game.player;

import java.util.List;

public interface Player {

    void accept(String card);

    void accept(List<String> cardList);

    /**
     * Show current num of cards
     */
    int getCardNum();

    List<String> getCardList();

    String getId();

    List<String> order(List<String> cardList);


    void print();

    /**
     * 出牌
     *
     * @param cards 打出的牌
     * @return true:出牌成功
     */
    boolean chuPai(String cards);

    /**
     * 身份标识
     *
     * @return 农民 or 地主
     */
    String getMark();
}
