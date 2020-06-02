package com.tochka.testtask.parsers;

import com.tochka.testtask.parsers.Operations.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class ChainOperationSelectorTest
{
	Operation operation = null;

	@Test
	void getOperation() throws Exception
	{
		operation = new ChainOperationSelector("body".split(":")).getOperation();
		assert(operation instanceof EmptyOperation);

		operation = new ChainOperationSelector("div:class(%layout_row_body)".split(":")).getOperation();
		assert(operation instanceof EndWithOperation);
		assert(ReflectionTestUtils.getField(operation, "attribute").equals("class"));
		assert(ReflectionTestUtils.getField(operation, "matchValue").equals("layout_row_body"));

		operation = new ChainOperationSelector("div:class(layout)".split(":")).getOperation();
		assert(operation instanceof EqualOperation);
		assert(ReflectionTestUtils.getField(operation, "attribute").equals("class"));
		assert(ReflectionTestUtils.getField(operation, "matchValue").equals("layout"));

		operation = new ChainOperationSelector("div:id(!)".split(":")).getOperation();
		assert(operation instanceof ExistOperation);
		assert(ReflectionTestUtils.getField(operation, "attribute").equals("id"));
		assert(ReflectionTestUtils.getField(operation, "matchValue").equals("!"));

		operation = new ChainOperationSelector("div:class(%layout%)".split(":")).getOperation();
		assert(operation instanceof LikeOperation);
		assert(ReflectionTestUtils.getField(operation, "attribute").equals("class"));
		assert(ReflectionTestUtils.getField(operation, "matchValue").equals("layout"));

		operation = new ChainOperationSelector("div:class(content-left%)".split(":")).getOperation();
		assert(operation instanceof StartWithOperation);
		assert(ReflectionTestUtils.getField(operation, "attribute").equals("class"));
		assert(ReflectionTestUtils.getField(operation, "matchValue").equals("content-left"));

		operation = new ChainOperationSelector("div:class()".split(":")).getOperation();
		assert(operation instanceof EqualOperation);
		assert(ReflectionTestUtils.getField(operation, "attribute").equals("class"));
		assert(ReflectionTestUtils.getField(operation, "matchValue").equals(""));
	}
}