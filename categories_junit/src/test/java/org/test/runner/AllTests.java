package org.test.runner;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

// This standard declaration is replace by ClasspathSuite.
// It's not necessary to declare explicitly all test classes.
//@RunWith(Suite.class)
//@SuiteClasses({
//  CategoriesJunit.class
//})
@RunWith(ClasspathSuite.class)
public class AllTests { }
