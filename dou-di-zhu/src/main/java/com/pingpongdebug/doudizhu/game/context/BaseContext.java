package com.pingpongdebug.doudizhu.game.context;

/**
 *
 */
public class BaseContext {

    public static ThreadLocal<PlatformContext> threadLocal = new ThreadLocal<>();

    public static void setContext(PlatformContext context) {
        threadLocal.set(context);
    }

    public static PlatformContext getContext() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static void init() {
        threadLocal.remove();
        setContext(new PlatformContext());
    }
}
