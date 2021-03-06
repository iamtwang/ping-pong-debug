package com.pingpongdebug.doudizhu.game.app;


import com.pingpongdebug.doudizhu.game.constant.Command;
import com.pingpongdebug.doudizhu.game.context.ContextHolder;
import com.pingpongdebug.doudizhu.game.context.PlatformContext;
import com.pingpongdebug.doudizhu.game.manager.PlatformManager;
import com.pingpongdebug.doudizhu.game.player.PlayerFactory;
import com.pingpongdebug.doudizhu.game.player.PlayerImpl;
import com.pingpongdebug.doudizhu.game.rule.CardNumRule;
import com.pingpongdebug.doudizhu.game.rule.RuleFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component("CardApp")
public class CardApp implements App {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardApp.class);

    @Value("${game.player.names}")
    private String[] playerNames;

    @Override
    public void start() throws Exception {

        printGameInfo();

        printGameRules();

        PlatformManager manager = (PlatformManager) PlatformManager.build()
                .playerInit(PlayerFactory.create(playerNames[0]),
                            PlayerFactory.create(playerNames[1]),
                            PlayerFactory.create(playerNames[2]))
                .cardInit()
                .shuffle()
                .giveCards()
                .register(new PlatformContext());

        RuleFactory.create(CardNumRule.class).checkCardNum(manager);

        //开始抢地主
        grabDiZhu(manager);

        //轮番出牌
        roundPlayingCard(manager);
    }

    private void roundPlayingCard(PlatformManager manager) throws Exception {
        //地主玩家开始出牌
        PlayerImpl player = manager.getPlayer(ContextHolder.getContext().getDiZhuId());
        //设置当前出牌玩家
        PlayerImpl currPlayer;
        while (true) {
            currPlayer = StringUtils.isBlank(ContextHolder.getContext().getCurrId()) ? player : manager.getNextPlayer();
            //打印提示信息
            printNoticeInfo(manager, currPlayer);
            Scanner scanner = new Scanner(System.in);
            String cards = scanner.nextLine();
            //出牌
            if (!currPlayer.playingCard(cards)) {
                continue;
            }
            //是否已经出完
            if (currPlayer.getCardList().size() == 0) {
                manager.settle(currPlayer);
                break;
            }
            ContextHolder.getContext().setCurrId(currPlayer.getId());
            //clear console
            this.cleanConsole();
        }
        retryGame();
    }

    private void printNoticeInfo(PlatformManager manager, PlayerImpl currPlayer) {
        //预获取下一个玩家,重要
        String currId = ContextHolder.getContext().getCurrId();
        ContextHolder.getContext().setCurrId(currPlayer.getId());
        LOGGER.info("轮到玩家：{}[{}] 出牌，手牌 {}" ,
                currPlayer.getId(),  currPlayer.getMark() ,currPlayer.order(currPlayer.getCardList()));

        LOGGER.info("提示：要不起请输入命令[pass],否则直接输入要打出的牌，多个请用空格分隔");
        if (StringUtils.isNotBlank(ContextHolder.getContext().getPreId())) {
            LOGGER.info("提示：上一个玩家是 {} [{}]" + " 打出的牌[{}]" +
                    " 下一个玩家 {} [{}]",
                    ContextHolder.getContext().getPreId(),
                    ContextHolder.getContext().getPreMark(),
                    ContextHolder.getContext().getPrePlayingCards(),
                    manager.getNextPlayer().getId(),
                    manager.getNextPlayer().getMark());
        }
        //设置回去,重要
        ContextHolder.getContext().setCurrId(currId);
    }

    private void grabDiZhu(PlatformManager manager) throws Exception {
        //随机获取一个玩家
        PlayerImpl randomPlayer = manager.getRandomPlayer();
        String firstId = randomPlayer.getId();
        randomPlayer.print();
        //设置当前玩家标识
        ContextHolder.getContext().setCurrId(randomPlayer.getId());
        //设置当前玩家
        PlayerImpl currPlayer = null;
        while (true) {
            LOGGER.info("***开始抢地主 命令[yes/no]***");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (!command.equalsIgnoreCase(Command.YES) && !command.equalsIgnoreCase(Command.NO)) {
                LOGGER.info("!!!请输入正确的命令[yes/no]!!!");
                continue;
            }
            if (command.equalsIgnoreCase(Command.YES)) {
                if (currPlayer == null) {
                    currPlayer = randomPlayer;
                }
                currPlayer.setDiZhu(true);
                break;
            } else {
                //获取下一个玩家
                currPlayer = manager.getNextPlayerRound(firstId);
                if (currPlayer == null) {
                    break;
                }

                currPlayer.print();
                //设置当前玩家标识
                ContextHolder.getContext().setCurrId(currPlayer.getId());
            }
        }

        //是否抢地主成功
        if (currPlayer != null && currPlayer.isDiZhu()) {
            grabSucc(manager, currPlayer);
        } else {
            grabFail();
        }
    }

    /**
     * 抢地主成功
     *
     * @param manager    平台管理器
     * @param currPlayer 玩家
     */
    private void grabSucc(PlatformManager manager, PlayerImpl currPlayer) {
        LOGGER.info("恭喜你，抢地主成功！");
        ContextHolder.init();
        ContextHolder.getContext().setDiZhuId(currPlayer.getId());
        //将底牌给当前玩家
        currPlayer.getCardList().addAll(manager.getBottomCards());
    }

    /**
     * 抢地主失败
     *
     * @throws Exception 异常
     */
    private void grabFail() throws Exception {
        //抢地主失败
        LOGGER.error("！没有人抢地主，是否重启游戏？[yes/no]");
        startOrFinished();
    }

    /**
     * 重启游戏
     *
     * @throws Exception 异常
     */
    private void retryGame() throws Exception {
        //抢地主失败
        LOGGER.info("是否再来一局？[yes/no]");
        startOrFinished();
    }


    private void startOrFinished() throws Exception {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        if (command.equalsIgnoreCase(Command.YES)) {
            start();
        } else {
            finished();
        }
    }


    private void printGameRules() throws InterruptedException {
        LOGGER.info("=============================开始介绍游戏规则===================================");
        LOGGER.info("本次游戏为三人局，包含抢地主，每人起手17张牌，底牌三张，地主可以拿到剩余底牌...");
        Thread.sleep(1000);
        LOGGER.info("每个玩家可以出单张,一对,三连张,三带一,三带二,顺子，连对，飞机，炸弹，王炸等牌型...");
        Thread.sleep(1000);
        LOGGER.info("每个玩家出的牌如果有多张，必须用空格分离...");
        Thread.sleep(1000);
        LOGGER.info("当上一个玩家打出牌后，下家可以根据提示选择相应命令或者打出对应牌型的牌...");
        Thread.sleep(1000);
        LOGGER.info("王炸>4张炸弹>其他...");
        LOGGER.info("=================================================================================");
        Thread.sleep(1000);
        LOGGER.info("游戏启动中，请稍等...");
        Thread.sleep(3000);
    }


    private void printGameInfo() throws InterruptedException {
        LOGGER.info("***********************欢迎进入小Y斗地主,祝您游戏体验愉快**********************");
        Thread.sleep(1000);
    }

    @Override
    public void finished() {
        LOGGER.info("***********************游戏结束**********************");
        System.exit(1);
    }

    @Override
    public void cleanConsole() {
        for (int i = 0; i < 5; i++) {
            LOGGER.info("");
        }
    }
}
