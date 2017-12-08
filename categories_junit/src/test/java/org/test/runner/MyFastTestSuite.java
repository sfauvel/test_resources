package org.test.runner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.test.category.FastTests;

@RunWith(Categories.class)
@Categories.IncludeCategory(FastTests.class)
@SuiteClasses( { AllTests.class } )
public class MyFastTestSuite { 

}