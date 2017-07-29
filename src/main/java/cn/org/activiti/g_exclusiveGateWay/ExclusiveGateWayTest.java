package cn.org.activiti.g_exclusiveGateWay;

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
public class ExclusiveGateWayTest {
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    /**
     * 部署流程定义
     */
    @Test
    public void deploy(){
        InputStream intputStreamBpmn = this.getClass().getResourceAsStream("exclusiveGateWay.bpmn");
        InputStream inputStreamPng = this.getClass().getResourceAsStream("exclusiveGateWay.png");
        Deployment deployment =  processEngine.getRepositoryService().createDeployment().name("排他网关").addInputStream("exclusiveGateWay.bpmn",intputStreamBpmn)
                .addInputStream("exclusiveGateWay.png",inputStreamPng).deploy();
        System.out.println("部署ID："+deployment.getId());
        System.out.println("部署名字："+deployment.getName());
    }
    /**
     * 启动流程
     */
    @Test
    public void createProcess(){
        String processDefinitionKey = "exclusiveGateWay";
        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID："+pi.getId());
        System.out.println("流程定义ID:"+pi.getProcessDefinitionId());

    }
    /**
     * 查看个人任务
     */
    @Test
    public void findMyTask(){

        String assignee = "王五";
        List<Task> list =  processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().asc().list();
        for (Task task:list) {
            System.out.println("任务ID："+task.getId());
            System.out.println("任务办理人："+task.getAssignee());
            System.out.println("流程实例ID："+task.getProcessInstanceId());
            System.out.println("流程定义ID："+task.getProcessDefinitionId());
            System.out.println("#####################################################");
        }
    }
    /**
     * 根据流程实例ID查询任务
     */
    @Test
    public void findTaskByPid(){
        String processInstanceId = "2501";
        List<Task> list = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().asc().list();
        for (Task task : list) {
            System.out.println("任务ID"+task.getId());
            System.out.println("任务执行人："+task.getAssignee());
            System.out.println("###################################");
        }
    }

    /**
     * 完成个人任务
     */
    @Test
    public void complete(){
        String taskId = "2604";
        Map<String,Object> valueMap = new HashMap<>();
        valueMap.put("money",1);
        processEngine.getTaskService().complete(taskId,valueMap);
        System.out.println(taskId+"任务已经完成");
    }
}
