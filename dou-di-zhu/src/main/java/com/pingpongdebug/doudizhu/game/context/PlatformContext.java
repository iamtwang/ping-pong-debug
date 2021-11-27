package com.pingpongdebug.doudizhu.game.context;


import com.pingpongdebug.doudizhu.game.enums.CardTypeEnum;

public class PlatformContext {
    /**
     * 上家标识
     */
    private String preId;
    /**
     * 上家身份
     */
    private String preMark;
    /**
     * 上家牌型
     */
    private CardTypeEnum preType;
    /**
     * 上家打出的牌
     */
    private String prePlayingCards;
    /**
     * 当前玩家id
     */
    private String currId;

    /**
     * 地主id
     */
    private String diZhuId;

    public String getPreId() {
        return preId;
    }

    public void setPreId(String preId) {
        this.preId = preId;
    }

    public String getPreMark() {
        return preMark;
    }

    public void setPreMark(String preMark) {
        this.preMark = preMark;
    }

    public CardTypeEnum getPreType() {
        return preType;
    }

    public void setPreType(CardTypeEnum preType) {
        this.preType = preType;
    }

    public String getPrePlayingCards() {
        return prePlayingCards;
    }

    public void setPrePlayingCards(String prePlayingCards) {
        this.prePlayingCards = prePlayingCards;
    }

    public String getCurrId() {
        return currId;
    }

    public void setCurrId(String currId) {
        this.currId = currId;
    }

    public String getDiZhuId() {
        return diZhuId;
    }

    public void setDiZhuId(String diZhuId) {
        this.diZhuId = diZhuId;
    }
}
