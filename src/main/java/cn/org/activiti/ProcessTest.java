package cn.org.activiti;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	@Test
	public void createTable() {
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
				.createStandaloneInMemProcessEngineConfiguration();
		processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		processEngineConfiguration.setJdbcUrl(
				"jdbc:mysql://localhost:3306/itcastactiviti?createDatabaseIfNotExist=true&userUnicode=true&charset=utf-8");
		processEngineConfiguration.setJdbcUsername("root");
		processEngineConfiguration.setJdbcPassword("root");
		processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println(processEngine);
	}

	@Test
	public void createTable2() {
		// 使用配置文件初始化表
		ProcessEngine processEngine = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
		System.out.println(processEngine);
	}

	@Test
	public void createTable3() {
		// 使用默认的方法默认调用activiti.cfg.xml文件进行数据库文件的初始化
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault()
				.buildProcessEngine();
		System.out.println(processEngine);
	}

	// 1.发布流程
	@Test
	public void deploy() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署对象有关的service
				.createDeployment()// 创建一个部署对象
				.name("helloworld入门程序")// 添加部署的名称
				.addClasspathResource("diagrams/helloworld.bpmn")// 从classpath的资源中加载，一次只能加载一个文件
				.addClasspathResource("diagrams/helloworld.png").deploy();// 完成部署
		System.out.println(deployment.getId());
	}

	// 2.启动流程
	@Test
	public void startProcess() {
		String processefinitionKey = "helloworld";
		ProcessInstance processInstance = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的service
				.startProcessInstanceByKey(processefinitionKey);// 使用流程定义的key启动流程实例，key为bpmn里面的id的值，使用key启动默认按照最新版本的流程定义启动
		System.out.println("流程实例Id:" + processInstance.getId());// 流程实例id
																// act_ru_execution
		System.out.println("流程定义id:" + processInstance.getProcessDefinitionId());// 流程定义ID
																					// act_re_procdef
	}

	// 查询当前人的个人任务
	@Test
	public void findMyProcessTask() {
		String assignee = "李四";
		List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service
						.createTaskQuery()//创建任务查询对象
						.taskAssignee(assignee)//指定个人任务查询，指定办理人
						.list();
		if(list!=null && list.size()>0){
			for(Task task:list){
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
	}
	//完成我的任务
	@Test
	public void completeMyProcess(){
		String taskId = "402";
		processEngine.getTaskService().complete(taskId);
		System.out.println("任务完成：任务id："+taskId);
	}
}
