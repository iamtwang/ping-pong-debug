package com.pingpongdebug.doudizhu.domain.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Tao
 */
public class RuleFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleFactory.class);

    public static <T> T create(Class<T> t) {
        try {
            return t.newInstance();
        } catch (Exception e) {
            LOGGER.error("failed to create rule {}", t);
            System.exit(1);
            return null;
        }
    }
}
