package com.gm.process;

import com.BaseTest;
import com.alibaba.fastjson.JSONObject;
import com.gm.process.service.TaskService;
import com.gm.process.vo.TaskOperateInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.task.api.Task;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.*;

/**
 * TaskServiceTest
 *
 * @author gongmin
 * @date 2025/10/30 9:06
 */
@Slf4j
public class TaskServiceTest extends BaseTest {
    @Resource
    private TaskService taskService;

    @Resource
    private RuntimeService runtimeService;

    @Test
    public void completeTask() {
        String taskId = "2445b75a-bed7-11f0-8e5b-00ff2df90d69";
        String operator = "云谷审批人";
        Map<String, Object> variables = new HashMap<>(2);
//        variables.put("yunguReviewer", "云谷审批人员");
//        variables.put("headquartersReviewer", "总部审批人员");
        variables.put("userId", "龚敏");
        variables.put("operateType", 1);
        variables.put("operateName", "打回");

        //String attachmentInfo = "3新疆项目需要本地化部署，需要将相关的软件包(共10个文件)拷出，目前软件包存放在服务器上，服务器ip地址：192.168.1.120，目录为：/data/xinjiang";
        String attachmentInfo = "服务器ip地址有问题";

        taskService.completeTask(taskId, operator, variables, attachmentInfo);
    }

    @Test
    public void completeTask1() {
        String taskId = "df4c4f94-c106-11f0-958d-00ff2df90d69";
        String operator = "部门经理";
        Map<String, Object> variables = new HashMap<>(4);
        variables.put("yunguReviewer", "云谷审批人员");
        variables.put("headquartersReviewer", "总部审批人员");
        variables.put("operateType", 1);
        variables.put("operateName", "同意");

        taskService.completeTask(taskId, operator, variables, "同意");
    }

    @Test
    public void getTaskOperates() {
        String processDefinitionId = "holiday-request:2:e330ac93-b534-11f0-b39a-00ff2df90d69";
        String taskKey = "requestHoliday";

        List<TaskOperateInfoVo> taskOperateInfoVos = taskService.getTaskOperates(processDefinitionId, taskKey);
        log.info(JSONObject.toJSONString(taskOperateInfoVos));
    }

    @Test
    public void getTaskInfo() {
        Task task = taskService.getTaskInfo("b4546c55-be09-11f0-b2e7-00ff2df90d69");
    }

    /**
     * 审批附属信息
     */
    @Getter
    @Setter
    public static class ReviewAttachmentInfo {
        /**
         * 审批意见
         */
        private String reviewComments;
    }

    /**
     * 请假附属信息
     */
    @Getter
    @Setter
    public static class RequestHolidayAttachmentInfo {
        /**
         * 请假天数
         */
        private int days;

        /**
         * 开始时间
         */
        private Date startDate;

        /**
         * 结束时间
         */
        private Date endDate;

        /**
         * 请假备注
         */
        private String remark;
    }
}
