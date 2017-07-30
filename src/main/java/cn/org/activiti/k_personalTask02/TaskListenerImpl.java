package cn.org.activiti.k_personalTask02;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListenerImpl implements TaskListener{
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("李四");
    }
}
