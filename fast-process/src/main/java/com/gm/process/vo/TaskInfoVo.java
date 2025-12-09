package com.gm.process.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 任务信息
 *
 * @author gongmin
 * @date 2025/10/30 9:51
 */
@Getter
@Setter
public class TaskInfoVo {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务开始时间
     */
    private Date startDate;

    /**
     * 任务结束时间
     */
    private Date endDate;

    /**
     * 任务执行人
     */
    private String operator;

    /**
     * 操作类型
     */
    private Integer operateType;

    /**
     * 操作名称
     */
    private String operateName;

    /**
     * 附属信息
     */
    private String attachmentInfo;
}
