package com.gm.process.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 任务信息
 *
 * @author gongmin
 * @date 2025/10/29 11:05
 */
@Getter
@Setter
public class TaskInfoBo {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务所属流程定义id
     */
    private String processDefinitionId;

    /**
     * 任务所属流程实例id
     */
    private String processInstanceId;

    /**
     * bpmn文件中task的id
     */
    private String taskKey;
}
