package com.gm.process.service;

import com.gm.process.config.ConfigTaskInfo;
import org.jetbrains.annotations.NotNull;

/**
 * 流程配置服务
 *
 * @author gongmin
 * @date 2025/10/29 14:13
 */
public interface ProcessConfigService {
    /**
     * 获取任务配置信息
     *
     * @param processDefinitionId 流程定义id
     * @param taskKey             bpmn文件中的task id
     * @return 任务配置信息
     */
    ConfigTaskInfo get(@NotNull String processDefinitionId, @NotNull String taskKey);
}
