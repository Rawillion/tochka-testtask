package com.tochka.testtask.parsers.Operations;

import org.w3c.dom.Node;

public class ExistOperation extends Operation
{
	public ExistOperation(String attribute, String matchValue)
	{
		super(attribute, matchValue);
	}

	@Override
	public boolean match(Node node)
	{
		String attributeValue = getAttributeValue(node);
		return (attributeValue != null && !attributeValue.equals(""));
	}
}
