package com.gm.process;

import com.alibaba.fastjson.JSONObject;
import com.gm.process.param.request.CompleteTaskRequest;
import liquibase.pro.packaged.C;

import java.util.HashMap;
import java.util.Map;

/**
 * OtherTest
 *
 * @author gongmin
 * @date 2025/10/30 17:09
 */
public class OtherTest {
    public static void main(String... args) {
        String taskId = "4fca3d5c-b56a-11f0-9e2c-00ff2df90d69";
        String operator = "部门经理";
        Map<String, Object> variables = new HashMap<>(4);
        variables.put("userId", "");
        variables.put("operateType", 0);
        variables.put("operateName", "同意");

        CompleteTaskRequest request = new CompleteTaskRequest();
        request.setTaskId(taskId);
        request.setOperator(operator);
        request.setVariables(variables);

        System.out.println(JSONObject.toJSONString(request));
    }
}
