package com.tochka.testtask.parsers.Operations;

import org.w3c.dom.Node;

public class EmptyOperation extends Operation
{
	public EmptyOperation(String attribute, String value)
	{
		super(attribute, value);
	}

	@Override
	public boolean match(Node node)
	{
		return true;
	}
}
