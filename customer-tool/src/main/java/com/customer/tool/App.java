package com.customer.tool;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import com.customer.tool.helper.FetchXml;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {

	public static void main(String[] args) throws ParserConfigurationException, SAXException {

		SpringApplication.run(App.class, args);
		String startFlowNodeId = args[0];
		String endFlowNodeId = args[1];

		FetchXml fetchXml = new FetchXml();
		fetchXml.JsonToXml(startFlowNodeId, endFlowNodeId);

	}

}
