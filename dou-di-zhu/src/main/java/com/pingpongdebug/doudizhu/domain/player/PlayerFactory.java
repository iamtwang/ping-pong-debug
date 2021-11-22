package com.pingpongdebug.doudizhu.domain.player;


public class PlayerFactory {

    public static Player create(String id) {
        return new PlayerModel(id);
    }
}
