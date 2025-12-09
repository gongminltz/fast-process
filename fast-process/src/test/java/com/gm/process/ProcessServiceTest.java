package com.gm.process;

import com.BaseTest;
import com.alibaba.fastjson.JSONObject;
import com.gm.process.bo.ProcessInfoBo;
import com.gm.process.constants.ProcessTypeEnum;
import com.gm.process.constants.TaskTypeEnum;
import com.gm.process.service.ProcessService;
import com.gm.process.vo.ProcessInfoVo;
import com.gm.process.vo.TaskInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程相关接口测试
 *
 * @author gongmin
 * @date 2025/10/28 10:54
 */
@Slf4j
public class ProcessServiceTest extends BaseTest {
    @Resource
    private ProcessService processService;

    @Resource
    private TaskService taskService;

    @Test
    public void deployProcessDefinition() {
        String bpmnFileName = "file-copy-out.bpmn20.xml";
        processService.deployDefinition(bpmnFileName);
    }

    @Test
    public void createInstance() {
        String processDefinitionId = "fileCopyMain:3:c2df6e4b-bec6-11f0-8274-00ff2df90d69";
        ProcessInfoBo processInfo = new ProcessInfoBo() {{
            setCreateUser("龚敏");
            setName("龚敏的文件拷出流程3");
            setType(ProcessTypeEnum.FILE_COPY_OUT_REQUEST);
            setRemark(null);
        }};
        Map<String, Object> variables = new HashMap<>();
        // 流转到请假者
        variables.put("userId", "龚敏");

        String processInstanceId = processService.createInstance(processDefinitionId, processInfo, variables);
        log.info("流程实例创建成功，流程实例id为" + processInstanceId);
    }

    @Test
    public void getProcess() {
        //cur 2441bfaa-bed7-11f0-8e5b-00ff2df90d69
        //root a552053d-bed6-11f0-8c14-00ff2df90d69
        String user = "总部审批人员";
        List<ProcessInfoVo> pendingProcessInfoVos = processService.getProcessInfo(user, TaskTypeEnum.PENDING);
        log.info(JSONObject.toJSONString(pendingProcessInfoVos));
    }

    @Test
    public void updateProcessInstanceStatus() {
        String processInstanceId = "88c73325-b473-11f0-bdcf-00ff2df90d69";
        int status = 1;

        processService.updateProcessInstanceStatus(processInstanceId, status);
    }

    @Test
    public void getProcessExecutionHistory() {
        String processInstanceId = "b7e23170-c5d7-11f0-9caa-00ff2df90d69";
        List<TaskInfoVo> taskInfoVos = processService.getProcessExecutionHistory(processInstanceId, true);
        log.info(JSONObject.toJSONString(taskInfoVos));
    }

    @Test
    public void getProcessCurTask() {
        Task task = taskService.createTaskQuery()
                .processInstanceId("fa360b91-c4f6-11f0-baa3-00ff2df90d69")
                .active()
                .singleResult();
        log.info(JSONObject.toJSONString(task));
    }
}
