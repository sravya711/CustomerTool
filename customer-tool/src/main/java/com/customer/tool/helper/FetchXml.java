package com.customer.tool.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.EndEventImpl;
import org.camunda.bpm.model.bpmn.impl.instance.UserTaskImpl;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import com.customer.tool.model.ApplicationProperties;
import com.customer.tool.model.JsonXML;

public class FetchXml {

	ApplicationProperties applicationProperties;

	public FetchXml(ApplicationProperties applicationProperties) {
		super();
		this.applicationProperties = applicationProperties;
	}

	public FetchXml() {
		super();
	}

	// @EventListener(ApplicationReadyEvent.class)
	public void JsonToXml(String startFlowNodeId, String endFlowNodeId)
			throws ParserConfigurationException, SAXException {

		String url = "https://elxkoom6p4.execute-api.eu-central-1.amazonaws.com/prod/engine-rest/process-definition/key/invoice/xml";
		RestTemplate restTemplate = new RestTemplate();
		JsonXML xml = restTemplate.getForObject(url, JsonXML.class);

		System.out.println("id = " + xml.getId());
		// System.out.println("BPMN20XML = " + xml.getBpmn20Xml());

		String xmlRep = xml.getBpmn20Xml();

		System.out.println(xmlRep);

		InputStream stream = new ByteArrayInputStream(xmlRep.getBytes());

		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(stream);

		ModelElementType taskType = modelInstance.getModel().getType(Task.class);
		Collection<ModelElementInstance> taskInstances = modelInstance.getModelElementsByType(taskType);

		UserTaskImpl userTaskImpl = null;
		EndEventImpl endEventImpl = null;
		for (ModelElementInstance taskInstance : taskInstances) {
			System.out.println("task Instance oculd be id" + taskInstance.getRawTextContent());
			System.out.println("task name: " + taskInstance.getElementType().getTypeName());
			if (userTaskImpl == null)
				userTaskImpl = taskInstance.getModelInstance().getModelElementById(startFlowNodeId);
			if (endEventImpl == null)
				endEventImpl = taskInstance.getModelInstance().getModelElementById(endFlowNodeId);
		}

		System.out.println("size of following nodes of userTaskImpl  " + userTaskImpl.getSucceedingNodes().list().size());

		for (FlowNode nextNode : userTaskImpl.getSucceedingNodes().list())
			System.out.println("following nodes of userTaskImpl  " + nextNode.getId());

		for (FlowNode previoiusNode : endEventImpl.getPreviousNodes().list()) {
			System.out.println("previous nodes of endEventImpl  " + previoiusNode.getId());
		}

		/*
		 * StartEvent start = (StartEvent)
		 * modelInstance.getModelElementById("StartEvent_1"); SequenceFlow sequenceFlow
		 * = (SequenceFlow) modelInstance.getModelElementById("SequenceFlow_1"); //
		 * SequenceFlow sequenceFlow_1 = (SequenceFlow) //
		 * modelInstance.getModelElementById("StartEvent_1"); FlowNode source =
		 * startFlowSeq.getSource(); FlowNode target = startFlowSeq.getTarget();
		 * 
		 * System.out.println("source " + source.getRawTextContent());
		 * 
		 * System.out.println("target " + target.toString());
		 * 
		 * Collection<SequenceFlow> outgoing = source.getOutgoing(); assert
		 * (outgoing.contains(sequenceFlow));
		 * 
		 * System.out.println("scscv " + outgoing.toString());
		 */
	}

}
