
Junit categories usage

http://junit.org/junit4/javadoc/4.12/org/junit/experimental/categories/Categories.html

With Maven test, we can launch category we want.

mvn test -Dtest-category=SlowTests

The class name is concat with the package where category are defined.
If no option  was given, there is one by default

When we launch test with Eclipse we use a specific class (into package org.test.runner).
She defines the category to use and classes to use (AllTests)

To avoid to have to explicitly define all classes needed, we use io.takari.junit:takari-cpsuite.
All tests classes into the classpath are added to the suite.  
