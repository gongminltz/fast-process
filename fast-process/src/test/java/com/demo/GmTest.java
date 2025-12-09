package com.demo;

import com.BaseTest;
import com.gm.process.bo.ProcessInfoBo;
import com.gm.process.constants.ProcessTypeEnum;
import com.gm.process.service.ProcessService;
import com.gm.process.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author gongmin
 * @date 2025/11/11 10:55
 */
@Slf4j
public class GmTest extends BaseTest {
    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ProcessService processService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    /**
     * 发布流程定义
     */
    @Test
    public void deployProcess() {
        repositoryService.createDeployment()
                .addClasspathResource("file-copy-main.bpmn20.xml")
                .addClasspathResource("info-review-sub.bpmn20.xml")
                .deploy();
    }

    /**
     * 创建流程实例
     */
    @Test
    public void createProcessInstance() {
        String processDefinitionId = "fileCopyMain:5:57a4d6f1-c13a-11f0-b005-00ff2df90d69";
        ProcessInfoBo processInfo = new ProcessInfoBo() {{
            setCreateUser("龚敏");
            setName("龚敏的文件拷出流程");
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
    public void completeTask1() {
        String taskId = "f88aec6e-c9ab-11f0-a911-00ff2df90d69";
        String operator = "总部审批人员";
        Map<String, Object> variables = new HashMap<>(2);
        variables.put("operateType", 0);
        variables.put("operateName", "同意");
        // variables.put("userId", "龚敏");

        //String attachmentInfo = "新疆项目需要本地化部署，需要将相关的软件包(共10个文件)拷出，目前软件包存放在服务器上，服务器ip地址：192.168.1.120，目录为：/data/xinjiang";
        String attachmentInfo = "同意";

        taskService.completeTask(taskId, operator, variables, attachmentInfo);
    }

    @Test
    public void completeTask() {
        String taskId = "d217a258-c9ab-11f0-81c9-00ff2df90d69";
        String operator = "部门经理";
        Map<String, Object> variables = new HashMap<>(6);
        variables.put("yunguReviewer", "云谷审批人员");
        variables.put("headquartersReviewer", "总部审批人员");
        variables.put("operateType", 0);
        variables.put("operateName", "同意");

        //String attachmentInfo = "3新疆项目需要本地化部署，需要将相关的软件包(共10个文件)拷出，目前软件包存放在服务器上，服务器ip地址：192.168.1.120，目录为：/data/xinjiang";
        //String attachmentInfo = "服务器ip地址有问题";

        taskService.completeTask(taskId, operator, variables, "同意");
    }

    @Test
    public void test() {
        List<String> procesInstanceIds = new ArrayList<String>() {{
            add("7b753cc7-c13a-11f0-8204-00ff2df90d69");
            add("da434138-c13a-11f0-b34b-00ff2df90d69");
        }};

        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceIdIn(procesInstanceIds)
                .orderByHistoricTaskInstanceEndTime()
                .asc()
                .list();
    }
}
