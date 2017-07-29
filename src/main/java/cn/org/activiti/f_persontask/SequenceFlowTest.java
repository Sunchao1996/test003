package cn.org.activiti.f_persontask;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/29.
 */
public class SequenceFlowTest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**部署流程定义（从inputStream）*/
    @Test
    public void deploymentProcessDefinition_inputStream(){
        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("sequenceFlow.bpmn");
        InputStream inputStreamPng = this.getClass().getResourceAsStream("sequenceFlow.png");
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("连线")
                .addInputStream("sequenceFlow.bpmn",inputStreamBpmn)
                .addInputStream("sequenceFlow.png",inputStreamPng).deploy();

        System.out.println("部署ID："+deployment.getId());//
        System.out.println("部署名称："+deployment.getName());//
    }

    /**启动流程实例*/
    @Test
    public void startProcessInstance(){
        String processDefintionKey = "sequenceFlow";
        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey(processDefintionKey);


        System.out.println("流程实例ID:"+pi.getId());//流程实例ID    101
        System.out.println("流程定义ID:"+pi.getProcessDefinitionId());//流程定义ID   helloworld:1:4
    }
    /**
     * 委托任务
     */
    @Test
    public void single(){
        processEngine.getTaskService().setAssignee("1904","张三");
        System.out.println("委托任务成功");
    }
    /**查询当前人的个人任务*/
    @Test
    public void findMyPersonalTask(){
        String assignee = "张三";
        List<Task> listTask = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime()
                .asc().list();
        for (Task task:listTask) {
            System.out.println("任务ID:"+task.getId());
            System.out.println("任务名称:"+task.getName());
            System.out.println("任务的创建时间:"+task.getCreateTime());
            System.out.println("任务的办理人:"+task.getAssignee());
            System.out.println("流程实例ID："+task.getProcessInstanceId());
            System.out.println("执行对象ID:"+task.getExecutionId());
            System.out.println("流程定义ID:"+task.getProcessDefinitionId());
            System.out.println("########################################################");
        }

    }

    /**完成我的任务*/
    @Test
    public void completeMyPersonalTask(){
        //任务ID
        String taskId = "1904";
        //完成任务的同时，设置流程变量，使用流程变量用来指定完成任务后，下一个连线，对应sequenceFlow.bpmn文件中${message=='不重要'}
        Map<String,Object> valueMap = new HashMap<>();
        valueMap.put("message","重要");
        processEngine.getTaskService().complete(taskId,valueMap);

        System.out.println("完成任务：任务ID："+taskId);
    }
}
