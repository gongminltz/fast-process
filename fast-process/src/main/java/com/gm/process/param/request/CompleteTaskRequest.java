package com.gm.process.param.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 完成任务请求参数
 *
 * @author gongmin
 * @date 2025/10/30 17:04
 */
@Getter
@Setter
public class CompleteTaskRequest {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;

    /**
     * 任务附属信息
     */
    private String attachmentInfo;
}
