package com.pingpongdebug.doudizhu.domain.app;

public interface App {

    void start() throws Exception;

    void finished();

    void cleanConsole();
}
