package com.gm.process.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * ApplicationUtils
 *
 * @author gongmin
 * @date 2025/10/30 16:51
 */
@Slf4j
public class ApplicationUtils {
    private static ApplicationContext applicationContext;

    /**
     * 保存Application上下文
     *
     * @param applicationContext application上下文
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        log.debug("<setApplicationContext> 设置application上下文");
        ApplicationUtils.applicationContext = applicationContext;
    }

    /**
     * 获取application上下文
     *
     * @return application上下文
     */
    public static ApplicationContext get() {
        return applicationContext;
    }
}
