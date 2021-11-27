package com.pingpongdebug.doudizhu.game.player;


public class PlayerFactory {

    public static Player create(String id) {
        return new PlayerImpl(id);
    }
}
