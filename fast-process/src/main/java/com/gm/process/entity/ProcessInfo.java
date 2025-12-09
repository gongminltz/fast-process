package com.gm.process.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 流程实例信息
 *
 * @author gongmin
 * @date 2025/10/29 9:56
 */
@Getter
@Setter
@TableName("gm_process_info")
public class ProcessInfo {
    private Long id;

    /**
     * 根流程实例id
     */
    private String rootProcessInstanceId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 类型
     */
    private String type;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程当前状态
     */
    private int status;

    /**
     * 流程描述
     */
    private String remark;
}
