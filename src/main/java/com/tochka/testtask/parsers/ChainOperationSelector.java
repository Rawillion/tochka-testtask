package com.tochka.testtask.parsers;

import com.tochka.testtask.parsers.Operations.*;

public class ChainOperationSelector
{
	private String operation = null;

	public ChainOperationSelector(String[] chainItem)
	{
		if (chainItem.length > 1)
			operation = chainItem[1];
	}

	public Operation getOperation()
	{
		//getAttribute("version")
		if (operation == null)
			return new EmptyOperation("", "");
		else
		{
			String[] operationParts = operation.split("\\(");
			String ruleAttribute = operationParts[0];
			operation = operationParts[1];
			operation = operation.substring(0, operation.length() - 1);// throw away last brace )
			if (operation.equals("!"))
				return new ExistOperation(ruleAttribute, operation);
			else if (operation.startsWith("%") && operation.endsWith("%"))
				return new LikeOperation(ruleAttribute, operation.substring(1, operation.length() - 1));
			else if (operation.startsWith("%"))
				return new EndWithOperation(ruleAttribute, operation.substring(1));
			else if (operation.endsWith("%"))
				return new StartWithOperation(ruleAttribute, operation.substring(0, operation.length() - 1));
			else return new EqualOperation(ruleAttribute, operation);
		}
	}
}
