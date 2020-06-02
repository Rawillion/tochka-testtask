package com.tochka.testtask.parsers.Operations;

import org.w3c.dom.Node;

public abstract class Operation
{
	protected String attribute;
	protected String matchValue;

	protected Operation(String attribute, String matchValue)
	{
		this.attribute = attribute.toLowerCase();
		this.matchValue = matchValue.toLowerCase();
	}

	protected String getAttributeValue(Node node)
	{
		Node attributeNode = node.getAttributes().getNamedItem(attribute);
		if (attributeNode == null)
		{
			return "";
		}
		return attributeNode.getNodeValue().toLowerCase();
	}

	public abstract boolean match(Node node);
}
