package com.gm.process.constants;

import lombok.Getter;

/**
 * 流程状态枚举
 */
@Getter
public enum ProcessStatusEnum {
    /**
     * 流程开始
     */
    PROCESS_START(0),

    /**
     * 流程成功结束
     */
    PROCESS_APPROVE_END(100),

    /**
     * 流程拒绝结束（流程终止）
     */
    PROCESS_REJECT_END(-1);

    private final int status;

    ProcessStatusEnum(int status) {
        this.status = status;
    }
}
