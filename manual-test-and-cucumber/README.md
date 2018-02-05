
# Handle manuel test with Cucumber

This project show a way to have all of our tests, manual or automated, write together with the same format.

We write tests with the gerkhin syntax.
With tags, we indicate which one are manual and which one are automated.

Automated tests know if they pass just executing them.

For the manual test, we have to pass them manually and indicate the result.
We just use a tag into the scenario (@ok, @ko) to indicate the result.
When we execute all tests, a specific glue make tests with @ok pass, and make fail tests with @ko.
Manuel tests without @ok or @ko return a pending result.

When we start a test campaign, we can create a specific branch and set the test result into it.

# Usage

To build project, run `build.sh`

It create project and copy jar to scripts folder.
It run : `mvn clean install assembly:single`

Put features into the `features` folder.

To generate a report, run `generate_report.sh`

You will find report into `reports` folder.

# Create report with Tzatziki

Tzatziki (https://github.com/Arnauld/tzatziki) generate a pdf report. 
We can use it to generate a pdf report with manual scenarios.

Build jar with command: `mvn clean install assembly:single`

Execute the jar with the command: `java -cp <JAR NAME>.jar org.sfvl.manualbdd.runner.TzatzikiReportMerge <FEATURE PATH>`

Change <JAR NAME> by the jar name generated and present into target. 
It's possible to rename the file to simplify it.

Specify where to find features with <FEATURE PATH> parameter.
If no value is given, the folder ./features is used.

The result could be found into reports folder created into the folder where the program was launched.
 