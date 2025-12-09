package com.gm.process.controller;

import com.gm.process.param.request.CompleteTaskRequest;
import com.gm.process.service.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务相关接口
 *
 * @author gongmin
 * @date 2025/10/30 17:02
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskService taskService;

    @PostMapping("/completeTask")
    public String completeTask(@RequestBody CompleteTaskRequest request) {
        taskService.completeTask(request.getTaskId(), request.getOperator(), request.getVariables(), request.getAttachmentInfo());
        return "ok";
    }
}
