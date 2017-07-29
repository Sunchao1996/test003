package cn.org.activiti.e_historyQuery;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;

public class HistoryQueryTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 查询历史流程实例
	 */
	@Test
	public void findHistoryProcessInstance() {
		String processInstanceId = "101";
		HistoricProcessInstance processInstance = processEngine.getHistoryService().createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).orderByProcessInstanceStartTime().asc().singleResult();
		System.out.println(processInstance.getId() + "  " + processInstance.getProcessDefinitionId());
	}

	/**
	 * 查询历史活动(包括其实和结束节点)
	 */
	@Test
	public void findHistoryActivity() {
		String processInstanceId = "101";
		List<HistoricActivityInstance> historicActivityInstances = processEngine.getHistoryService()
				.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
				.orderByHistoricActivityInstanceStartTime().asc().list();
		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			System.out.println("历史活动ID：" + historicActivityInstance.getActivityId());
			System.out.println("历史活动的流程实例ID：" + historicActivityInstance.getProcessInstanceId());
			System.out.println("历史活动的流程定义的ID：" + historicActivityInstance.getProcessDefinitionId());
		}
	}

	/**
	 * 查询历史任务（查看所有任务节点）
	 */
	@Test
	public void findHistoryTask() {
		String processInstanceId = "101";
		List<HistoricTaskInstance> historicTaskInstances = processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
				.orderByHistoricTaskInstanceStartTime().asc().list();
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			System.out.println("历史任务ID：" + historicTaskInstance.getId());
			System.out.println("历史任务的流程定义ID；" + historicTaskInstance.getProcessDefinitionId());
		}
	}

	/**
	 * 获取历史流程变量(某一次流程执行的时候产生的流程变量)
	 */
	@Test
	public void findHistoryVariable() {
		String processInstanceId = "701";
		List<HistoricVariableInstance> historicVariableInstances = processEngine.getHistoryService()
				.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).orderByVariableName().asc()
				.list();
		for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
			System.out.println("历史流程变量的ID："+historicVariableInstance.getId());
			System.out.println("历史流程变量的流程实例ID："+historicVariableInstance.getProcessInstanceId());
			System.out.println("历史流程变量的名称："+historicVariableInstance.getVariableName());
			System.out.println("历史流程变量的值："+historicVariableInstance.getValue());
			System.out.println("##############################################################");
		}
	}
}
