package cn.org.activiti.j_receiveTask;

import org.activiti.bpmn.model.Activity;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/7/29.
 */
public class ReceiveTaskProcess {
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    @Test
    public void delpoy(){
        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("receiveTask.bpmn");
        InputStream inputStreamPng = this.getClass().getResourceAsStream("receiveTask.png");
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("接受任务").addInputStream("receiveTask.bpmn",inputStreamBpmn)
                .addInputStream("receiveTask.png",inputStreamPng).deploy();
        System.out.println("部署ID："+deployment.getId());
        System.out.println("部署名称："+deployment.getName());
    }
    @Test
    public void startProcess(){
        String processKey = "receiveTask";
        ProcessInstance pi =  processEngine.getRuntimeService().startProcessInstanceByKey(processKey);
        System.out.println("流程实例ID："+pi.getId());
        System.out.println("流程定义ID："+pi.getProcessDefinitionId());
    }
    @Test
    public void findMyTask(){
        String assignee = "买家";
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().asc().list();
        for (Task task : list) {
            System.out.println("任务ID："+task.getId());
            System.out.println("流程定义ID:"+task.getProcessDefinitionId());
            System.out.println("流程实例ID:"+task.getProcessInstanceId());
            System.out.println("任务执行人："+task.getAssignee());
            System.out.println("任务名称:"+task.getName());
            System.out.println("##################################");
        }
    }

    /**
     * 查看执行对象
     */
    @Test
    public void findEx(){
        String processInstanceId = "3501";
     /*   Execution e = processEngine.getRuntimeService().createExecutionQuery().processInstanceId(processInstanceId).activityId("汇总当日销售额").singleResult();
        if(e == null){
            return ;
        }
        System.out.println("执行对象ID："+e.getId());
        System.out.println("流程实例ID："+e.getProcessInstanceId());
        System.out.println("活动ID："+e.getActivityId());
        System.out.println("###############################################");

        processEngine.getRuntimeService().setVariable(e.getId(),"当日销售额",123);

        processEngine.getRuntimeService().signal(e.getId());
*/
        Execution e1 = processEngine.getRuntimeService().createExecutionQuery().processInstanceId(processInstanceId).activityId("给老板发送短信").singleResult();
        int money = (int) processEngine.getRuntimeService().getVariable(e1.getId(),"当日销售额");
        System.out.println("执行对象ID："+e1.getId());
        System.out.println("当日销售额："+money);
        System.out.println("流程实例ID："+e1.getProcessInstanceId());
        System.out.println("活动ID："+e1.getActivityId());
        System.out.println("###############################################");
        processEngine.getRuntimeService().signal(e1.getId());
        List<Execution> list = processEngine.getRuntimeService().createExecutionQuery().processInstanceId(processInstanceId).list();
        if(list.size() <= 0){
            System.out.println("任务已经完成");
        }

    }
    @Test
    public void complete(){
        String taskId = "3202";
        processEngine.getTaskService().complete(taskId);
        System.out.println(taskId+"任务已经完成");

    }
}
