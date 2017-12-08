package org.test;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.test.category.FastTests;
import org.test.category.SlowTests;

public class CategoriesJunit {

	@Category(FastTests.class)
	@Test
	public void should_be_fast() throws Exception {
		
	}
	
	@Category(SlowTests.class)
	@Test
	public void should_be_slow() throws Exception {
		
	}
}
