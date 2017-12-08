package org.test.runner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.test.category.SlowTests;

@RunWith(Categories.class)
@Categories.IncludeCategory(SlowTests.class)
@SuiteClasses( { AllTests.class } )
public class MySlowTestSuite { 

}

