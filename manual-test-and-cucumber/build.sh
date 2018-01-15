
docker run -it --rm --name maven-project -v "$HOME/.m2":/root/.m2 -v "$PWD":/usr/src/project -w /usr/src/project maven:3.5.0-jdk-8 mvn clean install assembly:single

cp target/manual-test-cucumber-*-with-dependencies.jar scripts/cucumber2report.jar
