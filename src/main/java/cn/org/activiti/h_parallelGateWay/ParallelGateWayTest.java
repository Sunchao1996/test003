package cn.org.activiti.h_parallelGateWay;

import com.sun.imageio.plugins.common.InputStreamAdapter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class ParallelGateWayTest {
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    @Test
    public void deploy(){
        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("parallelGateWay.bpmn");
        InputStream inputStreamPng = this.getClass().getResourceAsStream("parallelGateWay.png");
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("并行网关").addInputStream("parallelGateWay.bpmn",inputStreamBpmn)
                .addInputStream("parallelGateWay.png",inputStreamPng).deploy();
        System.out.println("部署ID："+deployment.getId());
        System.out.println("部署名称："+deployment.getName());
    }
    @Test
    public void startProcess(){
        String processKey = "parallelGateWay";
        ProcessInstance pi =  processEngine.getRuntimeService().startProcessInstanceByKey(processKey);
        System.out.println("流程实例ID："+pi.getId());
        System.out.println("流程定义DI："+pi.getProcessDefinitionId());
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
    @Test
    public void complete(){
        String taskId = "3202";
        processEngine.getTaskService().complete(taskId);
        System.out.println(taskId+"任务已经完成");
    }
}
