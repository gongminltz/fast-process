package com.gm.process;

import com.BaseTest;
import com.alibaba.fastjson.JSONObject;
import com.gm.process.config.ConfigTaskInfo;
import com.gm.process.service.ProcessConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 流程相关接口测试
 *
 * @author gongmin
 * @date 2025/10/28 10:54
 */
@Slf4j
public class ProcessConfigServiceTest extends BaseTest {
    @Resource
    private ProcessConfigService processConfigService;

    @Test
    public void get() {
        String processDefinitionId = "copy_files_out:3:a21b65f8-b942-11f0-ba91-00ff2df90d69";
        String taskKey = "headquartersReview";

        ConfigTaskInfo taskInfo = processConfigService.get(processDefinitionId, taskKey);
        log.info(JSONObject.toJSONString(taskInfo));
    }
}
