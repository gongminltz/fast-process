package com.gm.process.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 配置文件中的process结构
 *
 * @author gongmin
 * @date 2025/10/30 16:33
 */
@Getter
@Setter
public class ConfigProcess {
    /**
     * 流程定义id
     */
    private String processDefinitionId;

    /**
     * 流程中的任务列表
     */
    private List<ConfigTaskInfo> taskList;
}
