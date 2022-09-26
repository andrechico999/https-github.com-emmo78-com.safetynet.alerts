# com.safetynet.alerts
The purpose of this application is to send information to emergency service systems.

## API - URL Endpoints

### POST (Create), PUT (Update) and DELETE

http://localhost:8080/person :  
body must be a json :  
{  
	"firstName": "FirstName",  
	"lastName": "LastName",  
	"address": "123 Street St",  
	"city": "City",  
	"zip": "12345",  
	"phone": "123-456-7890",  
	"email": "name@email.com"  
}  
  
http://localhost:8080/firestation  
body must be a json :  
{  
	"address": "Address",  
	"station": "1"  
}

http://localhost:8080/medicalRecord  
body must be a json :  
{  
	"firstName": "FistName",  
	"lastName": "LastName",  
	"birthdate": "MM/dd/yyyy",  
	"medications": [ "dcinameone:123mg", "dcinzmetwo:4546mg" ],  
	"allergies": [ "nameone", "nametwo" ]  
}  

### GET (Read) return a Json

http://localhost:8080/personInfo?firstName=FirstName&lastName=LastName    
return list of people with the same name living at the address of the specified person  
[  
	{  
		"lastName": "LastName",  
		"address": "123 Street St",  
		"age": "21",  
		"email": "name@email.com",  
		"medications": ["dcinameone:123mg", "dcinzmetwo:4546mg"],  
		"allergies": ["nameone", "nametwo"]  
	},  
	...  
]  

http://localhost:8080/firestation?stationNumber=1  
return list of people covered by the corresponding fire station  
with a count of the number of adults and the number of children (age <= 18 years old)  
[  
    {  
        "firstName": "FirstName",  
        "lastName": "LastName",  
        "address": "123 Street St",  
        "phone": "123-456-7890"  
    },  
	...  
    {  
        "adults": 5,  
        "children": 1  
    }  
]  

http://localhost:8080/phoneAlert?firestation=1  
return list of telephone numbers of residents served by the fire station  
no duplicate  
[  
    {  
        "phone": "841-874-7462"  
    },  
	...  
]  

http://localhost:8080/flood/stations?stations=1,2,3,4  
return list of all households served by the barracks. This list must group people by address.  
[  
    {  
        "address": "908 73rd St",  
        "lastName": "Peters",  
        "phone": "841-874-7462",  
        "age": "40",  
        "medications": [],  
        "allergies": []  
    },  
	...  
]  

http://localhost:8080/childAlert?address=1509 Culver St  
return list of children (age <= 18) living at this address with adult living with   
[  
    {  
        "firstName": "ChildFirstName",  
        "lastName": "ChildLastName",  
        "age": "10",  
        "adults": [  
            {  
                "firstName": "AdultFirstName",  
                "lastName": "AdultLastName",  
                "age": "38"  
            },  
			...  
        ]  
    },  
	...  
]  

http://localhost:8080/fire?address=112 Steppes Pl  
return list of inhabitants living at the given address as well as the number(s) of the fire station serving it  
[  
    {  
        "lastName": "LastName",  
        "phone": "8123-456-7890",  
        "age": "28",  
        "medications": [  
            "dcinameone:123mg",  
            "dcinametwo:45mg"  
        ],  
        "allergies": [  
            "allergiename"  
        ]  
    },  
	...  
    {  
        "stationNumbers": [  
            "3",  
            "4"  
        ]  
    }  
]  

http://localhost:8080/communityEmail?city=culver  
return list of email addresses of all the inhabitants of the city
no duplicate  
[
    {
        "email": "name@email.com"
    },
	...
]

## Prerequisites

What things you need to install the software and how to install them

- Java 11 (for SonarCloud) with 1.8 compliance
- apache Maven
- Spring Boot

Properties : src/main/resources/application.properties :
- need the property fileJson.path set to the path of source file.
- currently set to "./resources/input/data.json"
- log file  = "./logs/alerts.log"

File data.json is an ObjectNode with three keys "persons, firestations, medicalrecords".  
Each key maps an ArrayNode, a list of person, firestation and medicalrecord.

## Installing

A step by step series of examples that tell you how to get a development env running:

1.Install Java:

https://adoptium.net/temurin/releases?version=11

2.Install Maven:

https://maven.apache.org/install.html

3.Install Spring

https://spring.io/tools
or Eclipse Marketplace

## Testing

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






