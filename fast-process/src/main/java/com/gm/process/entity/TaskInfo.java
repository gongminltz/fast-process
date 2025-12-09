package com.gm.process.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 任务信息
 *
 * @author gongmin
 * @date 2025/10/29 16:52
 */
@Getter
@Setter
@TableName("gm_task_info")
public class TaskInfo {
    private Long id;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务完成时间
     */
    private Date completeDate;

    /**
     * 操作类型
     */
    private Integer operateType;

    /**
     * 操作名称
     */
    private String operateName;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 任务附件信息
     */
    private String attachmentInfo;

    /**
     * 参数
     */
    private String variables;
}
