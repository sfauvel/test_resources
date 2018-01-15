
= Doclet to generate test documentation

== Simple run

To build run : `build.sh`

To run, modify `execute.sh` and change `FOLDER_TO_DOC` to the folder where are test to document (absolute path).

Then run `execute.sh`

== Other

You can modify your pom to generate doc when run `mvn site`

Add to the pom.xml this configuration.

----
	<reporting>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-javadoc-plugin</artifactId>
				<version>3.0.0</version>

				<configuration>
					<doclet>doclet_living_doc.ExtractCommentsDoclet</doclet>

					<docletArtifact>
						<groupId>org.spike</groupId>
						<artifactId>doclet_living_doc</artifactId>
						<version>0.0.1-SNAPSHOT</version>
					</docletArtifact>
					<useStandardDocletOptions>false</useStandardDocletOptions>
				</configuration>
				<reportSets>
					<reportSet>

						<reports>
							<report>test-javadoc</report>
						</reports>
					</reportSet>
				</reportSets>
			</plugin>
		</plugins>
	</reporting>
----

Run the generation with

----
mvn site
----

The file doc.adoc will be generated into the folder ./target/site/testapidocs
