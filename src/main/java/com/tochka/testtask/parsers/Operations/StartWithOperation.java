package com.tochka.testtask.parsers.Operations;

import org.w3c.dom.Node;

public class StartWithOperation extends Operation
{
	public StartWithOperation(String attribute, String value)
	{
		super(attribute, value);
	}

	@Override
	public boolean match(Node node)
	{
		String attributeValue = getAttributeValue(node);
		return attributeValue.startsWith(matchValue);
	}
}
