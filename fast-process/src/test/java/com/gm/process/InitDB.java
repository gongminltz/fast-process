package com.gm.process;

import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

/**
 * 初始化数据库，请在执行该代码之前先创建数据库fast_process
 *
 * @author gongmin
 * @date 2025/10/28 10:36
 */
public class InitDB {
    public static void main(String... args) {
        // 1 创建ProcessEngineConfiguration实例,该实例可以配置与调整流程引擎的设置
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/fast_process?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2b8&nullCatalogMeansCurrent=true")
                .setJdbcUsername("gongmin")
                .setJdbcPassword("gongmin")
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        // 2 初始化ProcessEngine流程引擎实例，表不存在时创建相关表
        cfg.buildProcessEngine();
    }
}
