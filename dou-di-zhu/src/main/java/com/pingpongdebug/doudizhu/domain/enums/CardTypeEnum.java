package com.pingpongdebug.doudizhu.domain.enums;


public enum CardTypeEnum {
    /**
     * 单张
     */
    SINGLE(1),

    /**
     * 一对
     */
    PAIR(2),

    /**
     * 三连张
     */
    THREE_ONLY(3),
    /**
     * 三带一
     */
    THREE_WITH_ONE(4),
    /**
     * 三带二
     */
    THREE_WITH_TWO(5),
    /**
     * 顺子
     */
    SHUN_ZI(6),
    /**
     * 连对
     */
    SEQ_PAIR(7),
    /**
     * 飞机
     */
    PLANE(8),
    /**
     *
     */
    FOUR_WITH_TWO(9),

    FOUR_WITH_TWO_PAIR(10),
    /**
     * 炸弹
     */
    BOMB(11),
    /**
     * 王炸
     */
    KING_BOMB(12);


    private Integer type;

    public Integer getType() {
        return type;
    }

    CardTypeEnum(Integer type) {
        this.type = type;
    }
}
