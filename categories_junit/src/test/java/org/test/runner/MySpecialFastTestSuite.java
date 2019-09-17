package org.test.runner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.test.category.Slow;
import org.test.category.Special;

@RunWith(Categories.class)
@Categories.IncludeCategory(Special.class)
@Categories.ExcludeCategory(Slow.class)
@SuiteClasses( { AllTests.class } )
public class MySpecialFastTestSuite {

}

