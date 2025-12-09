package com.gm.process.constants;

import lombok.Getter;

/**
 * 流程实例类型枚举变量
 */
@Getter
public enum ProcessTypeEnum {
    /**
     * 由系统创建的子流程
     */
    SUB_PROCESS("-1"),

    /**
     * 请假流程
     */
    LEAVE_REQUEST("0"),

    /**
     * 文件拷出流程
     */
    FILE_COPY_OUT_REQUEST("1");

    private final String type;

    ProcessTypeEnum(String type) {
        this.type = type;
    }
}
