
# Function as App

[Article for this repo](https://chalise-arun.medium.com/write-once-deploy-everywhere-with-spring-cloud-function-7e3fa0ef4efdA)

https://chalise-arun.medium.com/write-once-deploy-everywhere-with-spring-cloud-function-7e3fa0ef4efd

An example set up for writing business logic once and deploying to different architectures using Spring Cloud Function. 

To run the api (application is saved in a MongoDB collection and event published to a kafka topic)
```
cd function-as-api
./gradlew bootRun
```

To run the functions as Spring Integration app (events polled from database and porcessed by s Spirng Integration application)

```
cd function-as-services
./gradlew bootRun --args='--spring.profiles.active=integration'
```

To run the functions as Spring Cloud Stream app (events processed as a Spring Cloud Stream application using Kafka)

```
cd function-as-services
./gradlew bootRun --args='--spring.profiles.active=CloudFunctionStream'
```

To test, submit an application
```
POST http://localhost:8080/submitApplication
Content-Type: application/json

{
  "firstName": "Joe",
  "lastName": "Bloggs",
  "email": "joe@bloggs.com",
  "amount": 100,
  "claimType": "FLOODS",
  "bankAccountName": "Joe Bloggs",
  "bankAccountNumber": "10876543",
  "bsb": "067432"
}

```
and see it processed by functions deployed as Spring Cloud Stream  or  Spring Integration applications.

