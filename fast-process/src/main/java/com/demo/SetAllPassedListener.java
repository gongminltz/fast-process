package com.demo;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * TODO
 *
 * @author gongmin
 * @date 2025/11/11 10:53
 */
public class SetAllPassedListener implements ExecutionListener {
    public void notify(DelegateExecution exec) {
        exec.setVariable("allPassed", true);
    }
}
