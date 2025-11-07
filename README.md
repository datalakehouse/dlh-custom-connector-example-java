
# Custom Connectors

**Custom Connectors** is a spring boot application which has concrete implementation of the connector.
It is designed with extensibility in mind, allowing you to add new connectors for different data providers with minimal effort.


## Configuration and Setup

Prerequisites:

1. Java (version 21)
2. Spring Boot 3.5.6
3. Maven


## How to run Custom Connector

1. Run the spring boot application directly using Maven

```bash
mvn spring-boot:run
```

2. Once it started successfully call run connector end point with required request parameters

```
curl --location 'http://localhost:8080/runConnector' \
--header 'Content-Type: application/json' \
--data '{
  "connectorName": "LIGHTSPEED_RETAIL_X",
  "connectorType": "REST",
  "csvRowLimit": 500,
  "threadPoolSize": 10
}'
```

3. In response, you will see following success message

```
Connector Lightspeed_Retailx Started!
```

4. All CSVs files generated under OUTPUT_PATH which is specified in Constants file 