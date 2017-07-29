package cn.org.activiti.d_processVariables;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessVariablesTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploymentProcessDefinition_zip() {
		InputStream inputStreambpmn = this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
		InputStream inputStreampng = this.getClass().getResourceAsStream("/diagrams/processVariables.png");
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("流程定义")
				.addInputStream("processVariables.bpmn", inputStreambpmn)// 使用资源文件的名称(要与资源文件的名称一致)和输入流完成部署
				.addInputStream("processVariables.png", inputStreampng).deploy();
		System.out.println(deployment);
	}

	/**
	 * 启动流程定义
	 */
	@Test
	public void startProcessInstance() {
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey("processVariables");
		System.out.println("流程定义ID：" + processInstance.getProcessDefinitionId());
		System.out.println("流程实例ID：" + processInstance.getId());
	}
	/**
	 * 根据流程实例ID查询正在执行的任务ID
	 */
	@Test
	public void findTaskId(){
		String processInstanceId = "701";
		List<Task> tasks =  processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).list();
		for (Task task : tasks) {
			System.out.println("任务ID："+task.getId());
		}
	}

	/**
	 * 执行个人任务
	 */
	@Test
	public void complateMyTask() {
		processEngine.getTaskService().complete("902");
		System.out.println("完成任务");
	}

	/**
	 * 查看流程是否完成
	 */
	@Test
	public void isProcessEnd() {
		ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId("1701").singleResult();
		if (processInstance == null) {
			System.out.println("任务已经完成");
		} else {
			System.out.println("任务正在执行");
		}
	}

	/**
	 * 设置流程变量 act_ru_variable 正在执行的流程变量表
	 */
	@Test
	public void setProcessVariable() {
		TaskService taskService = processEngine.getTaskService();
		// 任务ID1704
		String taskId = "704";
		// 基本类型设置流程变量
		 taskService.setVariableLocal(taskId, "请假天数", 3);//与任务ID绑定
		// 此任务执行完成之后不会出现在这个流程中
		 taskService.setVariableLocal(taskId, "请假天数", 5);//与任务ID绑定
		// 此任务执行完成之后不会出现在这个流程中
		 taskService.setVariable(taskId, "请假日期", new Date());
		 taskService.setVariable(taskId, "请假原因", "一起吃个饭");

		// 设置流程变量，使用javabean
//		Person person = new Person();
//		person.setId(3);
//		person.setName("翠花");
//		taskService.setVariable(taskId, "人员信息", person);
		System.out.println("设置变量成功");

		// taskService.complete(taskId);
	}

	/**
	 * 获取流程变量
	 */
	@Test
	public void getProcessVariable() {
		/** 用任务（正在执行） */
		TaskService taskService = processEngine.getTaskService();
		String taskId = "2404";
		// Date dateValue = (Date) taskService.getVariable(taskId, "请假日期");
		// String stringValue = (String) taskService.getVariable(taskId,
		// "请假原因");
		// System.out.println("请假日期:"+dateValue);
		// System.out.println("请假原因："+stringValue);
		Person person = (Person) taskService.getVariable(taskId, "人员信息");
		System.out.println(person);

	}

	/**
	 * 模拟设置和获取流程变量
	 */
	public void setAndGetrocessVariable() {
		RuntimeService runtimeService = processEngine.getRuntimeService();
		TaskService taskService = processEngine.getTaskService();
		// 设置流程变量
		// runtimeService.setVariable(executionId, variableName,
		// value);//表示使用执行对象id，和流程变量的名称，设置流程变量的值（一次只能设置一个值）
		// runtimeService.setVariables(executionId,
		// variables);//表示使用执行对象id和map集合来设置流程变量，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
		// taskService.setVariable(taskId, variableName, value);//使用任务id
		// taskService.setVariables(taskId, variables);//使用任务id
		// runtimeService.startProcessInstanceByKey(processDefinitionKey,
		// variables)//启动流程实例的时候设置流程变量
		// taskService.complete(taskId, variables);//使用完成任务来设置流程变量

		// 获取流程变量
		// runtimeService.getVariable(executionId,
		// variableName)//使用执行对象ID和流程变量的名称，获取流程变量的值
		// runtimeService.getVariables(executionId)//使用执行对象的ID获取所有的流程变量
		// runtimeService.getVariables(executionId,
		// variableNames)//使用执行对象id，获取流程变量的值，通过设置流程变量的名称，将流程变量的值获取到一个集合中
		// taskService.getVariable(taskId, variableName)//使用任务ID
		// taskService.getVariables(taskId)//使用任务ID
		// taskService.getVariables(taskId, variableNames)//使用任务ID
	}

	/**
	 * 历史流程实例查看
	 */
	@Test
	public void findHistoryTask() {
		List<HistoricProcessInstance> list = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processDefinitionKey("processVariables")
				.orderByProcessInstanceStartTime().asc().list();
		for (HistoricProcessInstance historicProcessInstance : list) {
			System.out.println("历史流程实例ID："+historicProcessInstance.getId());
			System.out.println("流程实例名称："+historicProcessInstance.getProcessDefinitionId());
		}
	}
}
