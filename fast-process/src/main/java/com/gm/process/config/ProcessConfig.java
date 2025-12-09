package com.gm.process.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * 请假流程配置
 *
 * @author gongmin
 * @date 2025/10/29 13:57
 */
@Configuration
@ConfigurationProperties(prefix = "process-config")
@Getter
@Setter
public class ProcessConfig {
    private List<ConfigProcess> process = Collections.emptyList();
}
