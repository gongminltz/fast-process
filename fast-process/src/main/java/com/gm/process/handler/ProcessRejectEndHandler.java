package com.gm.process.handler;

import com.gm.process.constants.ProcessStatusEnum;
import com.gm.process.service.ProcessService;
import com.gm.process.utils.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * 流程拒绝结束（流程终止）处理器
 *
 * @author gongmin
 * @date 2025/10/30 15:00
 */
@Slf4j
public class ProcessRejectEndHandler implements JavaDelegate {
    private static ProcessService processService;

    @Override
    public void execute(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        log.debug("<execute> 流程 {} 终止", processInstanceId);

        if (processService == null) {
            processService = ApplicationUtils.get().getBean(ProcessService.class);
        }

        processService.updateProcessInstanceStatus(processInstanceId, ProcessStatusEnum.PROCESS_REJECT_END.getStatus());
    }
}
