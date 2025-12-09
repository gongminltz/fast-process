package com.gm.process.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 任务操作信息
 *
 * @author gongmin
 * @date 2025/10/30 10:22
 */
@Getter
@Setter
public class TaskOperateInfoVo {
    /**
     * 操作类型
     */
    private int operateType;

    /**
     * 操作名称
     */
    private String operateName;
}
