# com.safetynet.alerts
The purpose of this application is to send information to emergency service systems.

## Getting Started

TODO

### Prerequisites

What things you need to install the software and how to install them

- Java 11 (for sonarCloud)
- apache maven 3.8.5

### Installing

A step by step series of examples that tell you how to get a development env running:

1.Install Java:

 https://adoptium.net/temurin/releases?version=11

2.Install Maven:

https://maven.apache.org/install.html

### Running App

TODO

### Testing

The app has unit tests and integration tests written.

To run the tests from maven, go to the folder that contains the pom.xml file and execute the below commands :

- $ mvn clean		→ clean ./target
- $ mvn test		→ run Unit Tests
- $ mvn verify		→ run Unit Tests, SIT and AIT
- $ mvn package		→ build .jar + Jacoco report in ./target/site/jacoco/index.html
					(run : $ java -jar target/alerts-0.0.1-SNAPSHOT.jar)
- $ mvn site 		→ put project reports in ./target/site/index.html
					( JavaDocs, SpotBugs, Surefire & Failsafe Reports, Jacoco & JaCoCo IT Reports)
- $ mvn surefire-report:report → surefire report in	./target/site/ surefire-report
- https://sonarcloud.io/project/overview?id=com.safetynet.alerts






