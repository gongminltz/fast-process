package com.gm.process.service.impl;

import com.gm.process.config.ConfigProcess;
import com.gm.process.config.ConfigTaskInfo;
import com.gm.process.config.ProcessConfig;
import com.gm.process.service.ProcessConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ProcessConfigServiceImpl
 *
 * @author gongmin
 * @date 2025/10/29 14:13
 */
@Slf4j
@Service
public class ProcessConfigServiceImpl implements ProcessConfigService {
    @Resource
    private ProcessConfig processConfig;

    @Override
    public ConfigTaskInfo get(@NotNull String processDefinitionId, @NotNull String taskKey) {
        log.debug("<get> 获取任务配置信息，processDefinitionId = {}， taskKey = {}", processDefinitionId, taskKey);

        ConfigProcess configProcess = processConfig.getProcess().stream()
                .filter(tmpConfigProcess -> processDefinitionId.equals(tmpConfigProcess.getProcessDefinitionId()))
                .findFirst()
                .orElse(null);
        if (configProcess == null) {
            log.warn("<get> 没有在配置文件中找到流程定义 {}", processDefinitionId);
            return null;
        }

        return configProcess.getTaskList().stream()
                .filter(configTaskInfo -> taskKey.equals(configTaskInfo.getKey()))
                .findFirst()
                .orElse(null);
    }
}
