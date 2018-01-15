
= Handle manuel test with Cucumber

This project show a way to have all of our tests, manual or automated, write together with the same format.

We write tests with the gerkhin syntax.
With tags, we indicate which one are manual and which one are automated.

Automated tests know if they pass just executing them.

For the manual test, we have to pass them manually and indicate the result.
We just use a tag into the scenario (@ok, @ko) to indicate the result.
When we execute all tests, a specific glue make tests with @ok pass, and make fail tests with @ko.
Manuel tests without @ok or @ko return a pending result.

When we start a test campaign, we can create a specific branch and set the test result into it.

= Usage

To build project, run build.sh

It create project and copy jar to scripts folder.
It run : _mvn clean install assembly:single_

Put features into the _features_ folder.

To generate a report, run _generate_report.sh_

You will find report into _reports_ folder.