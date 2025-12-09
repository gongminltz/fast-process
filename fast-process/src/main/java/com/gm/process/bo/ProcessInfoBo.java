package com.gm.process.bo;

import com.gm.process.constants.ProcessTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程信息
 *
 * @author gongmin
 * @date 2025/10/28 11:30
 */
@Getter
@Setter
public class ProcessInfoBo {
    /**
     * 创建人
     */
    private String createUser;

    /**
     * 类型
     */
    private ProcessTypeEnum type;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程描述
     */
    private String remark;
}
