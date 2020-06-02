package com.tochka.testtask.parsers.Operations;

import org.w3c.dom.Node;

public class EqualOperation extends Operation
{
	public EqualOperation(String attribute, String value)
	{
		super(attribute, value);
	}

	@Override
	public boolean match(Node node)
	{
		String attributeValue = getAttributeValue(node);
		return attributeValue.equals(matchValue);
	}
}
