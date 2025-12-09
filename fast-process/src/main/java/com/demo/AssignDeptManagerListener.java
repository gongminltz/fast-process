package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

/**
 * TODO
 *
 * @author gongmin
 * @date 2025/11/11 11:17
 */
@Slf4j
public class AssignDeptManagerListener implements TaskListener {
    public void notify(DelegateTask task) {
        String applicant = (String) task.getVariable("userId");
        log.info("<AssignDeptManagerListener> 文件拷出申请者 " + applicant);
        task.setAssignee("部门经理");
    }
}
