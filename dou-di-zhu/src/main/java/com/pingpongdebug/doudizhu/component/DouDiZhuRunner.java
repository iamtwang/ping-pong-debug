package com.pingpongdebug.doudizhu.component;


import com.pingpongdebug.doudizhu.domain.app.CardApp;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DouDiZhuRunner implements ApplicationRunner {

    @Resource(name = "CardApp")
    private CardApp cardApp;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        cardApp.start();
    }
}
