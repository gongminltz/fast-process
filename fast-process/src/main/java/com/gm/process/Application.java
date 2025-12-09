package com.gm.process;


import com.gm.process.utils.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 *
 * @author gongmin
 * @date 2023/1/12 11:09
 */
@Slf4j
@EnableTransactionManagement
@EnableAsync
@SpringBootApplication
@MapperScan("com.gm.process.mapper")
public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        ApplicationUtils.setApplicationContext(applicationContext);
        log.info("<main> start");
    }
}
