package com.pingpongdebug.doudizhu.game.player;


import com.pingpongdebug.doudizhu.game.constant.Command;
import com.pingpongdebug.doudizhu.game.context.BaseContext;
import com.pingpongdebug.doudizhu.game.context.PlatformContext;
import com.pingpongdebug.doudizhu.game.enums.CardTypeEnum;
import com.pingpongdebug.doudizhu.game.rule.CardCompareRule;
import com.pingpongdebug.doudizhu.game.rule.CardTypeRule;
import com.pingpongdebug.doudizhu.game.rule.RuleFactory;
import com.pingpongdebug.doudizhu.game.utils.CardUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;



public class PlayerModel implements Player {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerModel.class);
    /**
     * 是否地主
     */
    private boolean diZhu;

    /**
     * 玩家标识
     */
    private String id;

    /**
     * 玩家持有的牌
     */
    private List<String> cardList;

    /**
     * 当前牌的数量
     */
    private int cardNum;


    public PlayerModel(String id) {
        this.id = id;
        cardList = new ArrayList<>();
    }

    @Override
    public void accept(String card) {
        cardList.add(card);
    }

    @Override
    public void accept(List<String> cards) {
        cardList.addAll(cards);
    }

    @Override
    public int getCardNum() {
        return cardList.size();
    }

    @Override
    public List<String> getCardList() {
        return cardList;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<String> order(List<String> cardList) {
        return CardUtil.cardRecombineShow(cardList);
    }

    @Override
    public void print() {
        LOGGER.info("玩家：" + getId() + "-" + getMark());
        LOGGER.info("纸牌：" + order(cardList) + ",牌数：" + getCardNum());
    }

    @Override
    public boolean playingCard(String cards) {
        try {
            //是否pass
            if (StringUtils.isNotBlank(cards) && cards.equalsIgnoreCase(Command.PASS)) {
                return true;
            }
            //出牌规则校验
            CardTypeEnum cardTypeEnum = RuleFactory.create(CardTypeRule.class).checkCardType(cards, this);
            if (cardTypeEnum == null) {
                LOGGER.error("错误：！出牌有误，请检查打出的牌！");
                return false;
            }
            PlatformContext context = BaseContext.getContext();
            //是否第一个出牌
            if (StringUtils.isBlank(context.getPreId()) || this.getId().equalsIgnoreCase(context.getPreId())) {
                //出牌成功,设置context
                updateContext(cards, cardTypeEnum, context);
                return true;
            }
            //比较出牌大小
            if (!RuleFactory.create(CardCompareRule.class).compare(context, cardTypeEnum, cards)) {
                LOGGER.error("错误：！出牌有误，请检查打出的牌！");
                return false;
            }
            //出牌成功,设置context
            updateContext(cards, cardTypeEnum, context);
            return true;
        } catch (Exception e) {
            LOGGER.error("未识别命令，请重新输入");
            return false;
        }
    }

    @Override
    public String getMark() {
        return isDiZhu() ? "地主" : "农民";
    }

    public boolean isDiZhu() {
        return diZhu;
    }

    public void setDiZhu(boolean diZhu) {
        this.diZhu = diZhu;
    }

    /**
     * 更新容器
     *
     * @param cards        打出的牌
     * @param cardTypeEnum 牌型
     * @param context      容器
     */
    private void updateContext(String cards, CardTypeEnum cardTypeEnum, PlatformContext context) {
        context.setPreId(this.getId());
        context.setPreMark(this.getMark());
        context.setPreType(cardTypeEnum);
        context.setPreGiveCards(cards);
        //除去自己手中已经打出的牌
        List<String> leaveList = CardUtil.leaveConvert(cards);
        leaveList.forEach(e -> getCardList().remove(e));
    }


    @Override
    public String toString() {
        return "玩家：" + getId() + ",纸牌：" + cardList + ",牌数：" + cardNum + ",是否地主：" + diZhu;
    }
}
