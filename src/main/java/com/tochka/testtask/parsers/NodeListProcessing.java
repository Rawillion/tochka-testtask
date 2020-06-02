package com.tochka.testtask.parsers;

import com.tochka.testtask.parsers.Operations.Operation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

class NodeListProcessing
{
	private NodeList nodeList;
	private String name;
	private Operation operation;

	public NodeListProcessing(NodeList nodeList, String chainItem)
	{
		this.nodeList = nodeList;
		String[] parseRule = chainItem.split(":");
		name = parseRule[0];
		operation = new ChainOperationSelector(parseRule).getOperation();
	}

	public List<Node> getMatchedNodes()
	{
		List<Node> matchesNodes = new ArrayList<>();
		for (int i = 0; i < nodeList.getLength(); ++i)
		{
			Node node = nodeList.item(i);
			if (node.getNodeName().equals(name) && operation.match(node))
				matchesNodes.add(node);
		}
		return matchesNodes;
	}
}
