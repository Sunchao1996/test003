package cn.org.activiti.c_processInstace;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ProcessInstanceTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 从zip文件部署流程定义
	 */
	@Test
	public void deploymentProcessDefinition_zip() {
		InputStream in = getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipIn = new ZipInputStream(in);
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("流程定义")
				.addZipInputStream(zipIn).deploy();
		System.out.println("部署名称：" + deployment.getName());
		System.out.println("部署id：" + deployment.getId());
	}
	/**
	 * 启动流程定义
	 * act_ru_execution 正在执行的对象表 
	 * act_hi_procinst 流程实例的历史表
	 * act_ru_task 正在执行的任务表（只有节点是userTask的时候才有数据）
	 * act_hi_taskinst 任务历史表（只有节点是userTask的时候才有数据）
	 * act_hi_actinst 所有活动节点的历史表
	 * 1.如果是单例流程（没有分支和聚合）那么流程实例ID的执行对象ID是相同的
	 * 2.一个流程定义实例只有一个，执行对象可以有多个（存在分支和聚合的时候）
	 * 
	 * 
	 * 流程实例，一个流程从开始到结束
	 * 执行对象，流程执行的每一个分支
	 */
	@Test
	public void startProcessIntance(){
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("helloworld");
		System.out.println("流程实例id:"+processInstance.getId());
		System.out.println("流程定义id："+processInstance.getProcessDefinitionId());
	}
	/**
	 * 查询当前的个人任务
	 */
	@Test
	public void findMyPersonalTask(){
		String assignee = "张三";
		processEngine.getTaskService().createTaskQuery().taskAssignee(assignee)
//		.taskCandidateGroup(candidateGroup)//租任务的办理人查询
//		.processDefinitionId(processDefinitionId)//流程定义ID查询
//		.processInstanceId(processInstanceId)//流程实例ID查询
//		.executionId(executionId)//执行对象的id
		;
	}
	/**
	 * 完成任务
	 */
	@Test
	public void completeMyProcess(){
		String taskId = "1302";
		processEngine.getTaskService().complete(taskId);
		System.out.println("完成任务：任务ID："+taskId);
	}
	/**
	 * 查询流程状态（判断流程正在执行还是结束）
	 */
	@Test
	public void isProcessEnd(){
		String processInstanceId = "1101";
		ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if(processInstance == null){
			System.out.println("流程已经结束");
		}else{
			System.out.println("流程还没结束");
		}
	}
	/**
	 * 查新历史任务act_hi_taskinst
	 */
	@Test
	public void findHistoryTask(){
		String taskAssignee = "张三";
		List<HistoricTaskInstance> hil = processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(taskAssignee).list();
		for (HistoricTaskInstance historicTaskInstance : hil) {
			System.out.println(historicTaskInstance.getId()+"	"+historicTaskInstance.getProcessInstanceId());
		}
	}
	/**
	 * 查询历史流程实例
	 */
	@Test
	public void findHistoryProcessIntance(){
		String processInstanceId = "1101";
		HistoricProcessInstance historicProcessInstance= processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		System.out.println(historicProcessInstance.getId()+" "+historicProcessInstance.getStartActivityId());
	}
}
