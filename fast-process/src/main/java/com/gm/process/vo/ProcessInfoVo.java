package com.gm.process.vo;

import com.gm.process.entity.ProcessInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程信息
 *
 * @author gongmin
 * @date 2025/10/29 16:32
 */
@Getter
@Setter
public class ProcessInfoVo extends ProcessInfo {
    /**
     * 待处理任务id
     */
    private String taskId;

    /**
     * bpmn文件中待处理task的id
     */
    private String taskKey;

    /**
     * 流程定义id
     */
    private String processDefinitionId;
}
