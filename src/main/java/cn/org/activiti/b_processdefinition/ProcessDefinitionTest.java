package cn.org.activiti.b_processdefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ProcessDefinitionTest {
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
	 * 查看流程定义
	 */
	@Test
	public void queryProcessDefinition() {
		List<ProcessDefinition> processDefinitions = processEngine.getRepositoryService().createProcessDefinitionQuery()
				// .deploymentId(deploymentId)//部署对象ID
				// .processDefinitionName(processDefinitionName)//流程定义名字精确查询
				// .processDefinitionId(processDefinitionId)//流程定义ID
				// .processDefinitionKey(processDefinitionKey)//流程定义的key
				// .processDefinitionVersion(processDefinitionVersion)//流程的版本
				.orderByDeploymentId()// 根据流程定义的id排序
				.desc()// 降序排序
				.list();// 返回列表
		// .singleResult()//返回单个结果集
		// .count()//返回结果集数量
		// .listPage(firstResult, maxResults)//分页查询结果集
		for (ProcessDefinition processDefinition : processDefinitions) {
			System.out.println("流程定义的ID：" + processDefinition.getId());
			System.out.println("流程定义的名称：" + processDefinition.getName());
			System.out.println("流程定义的key：" + processDefinition.getKey());
			System.out.println("流程定义的版本：" + processDefinition.getVersion());
			System.out.println("流程定义的bpmn文件：" + processDefinition.getResourceName());
			System.out.println("流程定义的png文件：" + processDefinition.getDiagramResourceName());
			System.out.println("部署对象ID：" + processDefinition.getDeploymentId());
			System.out.println("############################################################");
		}
	}

	/**
	 * 删除流程定义
	 */
	@Test
	public void deleteDeployment() {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		// repositoryService.deleteDeployment("101");//可以删除没有运行中的流程定义
		repositoryService.deleteDeployment("101", true);// 可以删除运行中的流程定义
	}

	/**
	 * 查看流程定义的流程图
	 * 
	 * @throws IOException
	 */
	@Test
	public void viewPic() throws IOException {
		String deploymentId = "801";
		List<String> names = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
		String resourceName = "";
		for (String string : names) {
			if (string.indexOf(".png") != 0) {
				resourceName = string;
			}
		}
		InputStream inputStream = processEngine.getRepositoryService().getResourceAsStream(deploymentId, resourceName);
		File file = new File("D:\\1\\" + resourceName);
		FileUtils.copyInputStreamToFile(inputStream, file);
	}

	/**
	 * 查看最新版的流程定义
	 */
	@Test
	public void findLastVersionProcessDefinition() {
		List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().asc().list();
		Map<String, ProcessDefinition> map = new HashMap<String, ProcessDefinition>();
		for (ProcessDefinition processDefinition : list) {
			map.put(processDefinition.getKey(), processDefinition);
		}
		List<ProcessDefinition> pdList = new ArrayList<>(map.values());
		for (ProcessDefinition processDefinition : pdList) {
			System.out.println("流程定义ID:" + processDefinition.getId());// 流程定义的key+版本+随机生成数
			System.out.println("流程定义的名称:" + processDefinition.getName());// 对应helloworld.bpmn文件中的name属性值
			System.out.println("流程定义的key:" + processDefinition.getKey());// 对应helloworld.bpmn文件中的id属性值
			System.out.println("流程定义的版本:" + processDefinition.getVersion());// 当流程定义的key值相同的相同下，版本升级，默认1
			System.out.println("资源名称bpmn文件:" + processDefinition.getResourceName());
			System.out.println("资源名称png文件:" + processDefinition.getDiagramResourceName());
			System.out.println("部署对象ID：" + processDefinition.getDeploymentId());
			System.out.println("#########################################################");
		}
	}
	/**
	 * 删除相同的key下的所有流程定义
	 */
	@Test
	public void deleteProcessDefinitionByKey(){
		List<ProcessDefinition> list = 
				processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("helloworld").list();
		for (ProcessDefinition processDefinition : list) {
			processEngine.getRepositoryService().deleteDeployment(processDefinition.getDeploymentId(), true);
		}
		System.out.println("删除成功");
	}
}
