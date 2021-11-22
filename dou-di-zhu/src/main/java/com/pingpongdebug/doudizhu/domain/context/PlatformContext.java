package com.pingpongdebug.doudizhu.domain.context;


import com.pingpongdebug.doudizhu.domain.enums.CardTypeEnum;

public class PlatformContext implements Context {
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
     * 上家打出的牌，空格分隔
     */
    private String preGiveCards;
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

    public String getPreGiveCards() {
        return preGiveCards;
    }

    public void setPreGiveCards(String preGiveCards) {
        this.preGiveCards = preGiveCards;
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
