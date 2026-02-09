# JPM Asset Movement Engine

## Overview
This project implements a simplified **asset movement / settlement enrichment service** using **Spring Boot and Java 17**.

The service accepts a trade request, enriches it using **Standard Settlement Instructions (SSI)**, and produces a settlement message.

The implementation focuses on:
- clean architecture
- testable business logic
- validation and exception handling
- extensibility for future enhancements

---

## Tech Stack
- Java 17
- Spring Boot
- Maven
- JUnit 5
- Mockito
- Jakarta Validation

---

## Architecture

controller → service

service → domain

service → repository


### Layer responsibilities
- **Controller**  
  HTTP API, request/response mapping, validation
- **Service**  
  Business logic, enrichment, orchestration
- **Repository**  
  Data access abstraction (in-memory implementation)
- **Domain**  
  Immutable core models (records / value objects)

This separation allows the system to evolve (e.g. database-backed persistence) without impacting business logic.

---

## Features Implemented
- REST API for settlement creation and retrieval
- SSI enrichment using reference data
- Supporting information formatting
- Duplicate `tradeId` detection
- Request validation
- Centralized exception handling
- Unit tests for core business logic

---

## How to Run

### Prerequisites
- Java 17 or later
- Maven (or use the Maven wrapper included)

### Start the application
```bash
./mvnw spring-boot:run
```

The Application will start on `http://localhost:8080`

### API Endpoints
- **Create Settlement**
  - `POST /api/settlements`
  - Request Body:
  ```json
  {
  "tradeId": "16846548",
  "ssiCode": "OCBC_DBS_1",
  "amount": 12894.65,
  "currency": "USD",
  "valueDate": "20022020"
  }
  ```

    - Response:
  ```json
  {
  "tradeId": "16846548",
  "messageId": "uuid",
  "amount": 12894.65,
  "currency": "USD",
  "valueDate": "20022020",
  "payerParty": {
    "accountNumber": "438421",
    "bankCode": "OCBCSGSGXXX"
  },
  "receiverParty": {
    "accountNumber": "05461368",
    "bankCode": "DBSSGB2LXXX"
  },
  "supportingInformation": "/BNF/FFC-4697132"
  }
  ```

- **Get Settlement by Trade ID**
    - `GET /api/settlements/{tradeId}`
  
    - Response:
  ```json
  {
  "tradeId": "16846548",
  "messageId": "d82e7a94-217a-4ea1-84bc-f0d173a85a2d",
  "amount": 12894.65,
  "currency": "USD",
  "valueDate": "20022020",
  "payerParty": {
    "accountNumber": "438421",
    "bankCode": "OCBCSGSGXXX"
  },
  "receiverParty": {
    "accountNumber": "05461368",
    "bankCode": "DBSSGB2LXXX"
  },
  "supportingInformation": "/BNF/FFC-4697132"
    }
  ```
  


### Error handling
#### 1: Domain / Service layer (business failures)
Business rule violations are expressed using custom runtime exceptions.
- SsiNotFoundException
  - Thrown when ssiCode lookup fails in SettlementService.create()
- SettlementNotFoundException
  - Thrown when tradeId does not exist in SettlementService.getByTradeId()
- DuplicateTradeIdException
  - Thrown when trying to persist a second settlement with the same tradeId 


#### 2: Controller layer (validation failures)
Incoming request validation is handled automatically by Spring + Jakarta Validation.
- Controller uses: @Valid @RequestBody CreateSettlementRequest, If the request body is incomplete/invalid:
  - Spring throws MethodArgumentNotValidException
  
#### HTTP status mapping
- Duplicate tradeId:
  - Status: `409`
- SSI not found:
  - Status: `404`
- Validation errors:
  - Status: `400`
- Centralized exception-to-HTTP mapping:
  - All exceptions are handled by GlobalExceptionHandler(@RestControllerAdvice) which returns a consistent error response format:
```json
{
"message": "error description",
"timestamp": "2026-02-05T22:12:41Z"
}
```
---  
## Unit Tests

### 1. SupportingInformationFormatterTest

**What it tests**  
Pure formatting rules with no Spring context and no mocks.

**Class under test**
- SupportingInformationFormatter

**Method tested**
- format(String raw)

**Covered scenarios**
-  Converts key/value format into the required settlement format
    - Input: "BNF:FFC-4697132"
    - Output: "/BNF/FFC-4697132"

-  Returns input as-is when there is no delimiter (`:`)
    - Input: "HELLO"
    - Output: "HELLO"

-  Handles blank or null input safely
    - Input: null / " "
    - Output: ""

### 2. SettlementServiceTest

**What it tests**  
Business orchestration and enrichment behavior using unit tests with mocks.

**Class under test**
- SettlementService

**Methods tested**
- create(TradeRequest request)
- getByTradeId(String tradeId) (if present)

**Testing approach**
- SsiRepository mocked using Mockito to control SSI lookup behavior
- SettlementMessageRepository mocked using Mockito to control persistence behavior
- Real SupportingInformationFormatter used to validate formatting end-to-end within the service

**Covered scenarios**

-  create() happy path
    - Given a valid TradeRequest
    - SSI lookup returns a valid SsiRecord
    - Service builds a SettlementMessage with:
        - payer and receiver derived from SSI
        - correctly formatted supportingInformation
        - generated messageId (non-null)
    - messageRepository.saveIfAbsent() is called exactly once
    - Returned message fields match expected values

-  create() failure path — SSI missing
    - SSI lookup returns Optional.empty()
    - Service throws SsiNotFoundException
    - Verifies repository save is **not** called (never())

#### Command for running tests
```bash
./mvnw test
```

---
### Design Decisions and Notes
- **In-memory repository**: For simplicity, an in-memory implementation is used. This can be easily replaced with a database-backed repository in the future.
- Domain models are immutable to avoid accidental state mutation
- Business logic is isolated in the service layer to keep controllers thin
- Validation is performed at the controller level using Jakarta Validation annotations


### Future Enhancements(as of Feb 9th 2026)
- Replace in-memory repositories with database-backed persistence (JPA / JDBC)
- Swagger / OpenAPI documentation
- Add controller tests using MockMvc
- Idempotency and concurrency handling
- Metrics and health checks

---
## Example API Requests
### Create settlement (PowerShell, expect 200)
```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/settlements" `
  -ContentType "application/json" `
  -Body '{
    "tradeId":"16846548",
    "ssiCode":"OCBC_DBS_1",
    "amount":12894.65,
    "currency":"USD",
    "valueDate":"20022020"
  }'
```
### Duplicate tradeId error (expect 409 conflict)
Duplicate 'tradeId' enter
```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/settlements" `
  -ContentType "application/json" `
  -Body '{
    "tradeId":"16846548",
    "ssiCode":"OCBC_DBS_1",
    "amount":12894.65,
    "currency":"USD",
    "valueDate":"20022020"
  }'
```
### SSI not found (expect 404 Not Found)
SSI mismatch
```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/settlements" `
  -ContentType "application/json" `
  -Body '{
    "tradeId":"2",
    "ssiCode":"ocbc_dbs_999",
    "amount":1,
    "currency":"USD",
    "valueDate":"20022020"
  }'
```

### Validation failure (expect 400 Bad Request)
request body incomplete or invalid
```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/settlements" `
  -ContentType "application/json" `
  -Body '{
    "tradeId":"",
    "ssiCode":"OCBC_DBS_1",
    "amount":-1,
    "currency":"USDD",
    "valueDate":"20022020"
  }'
```

### Get settlement by tradeId (expect 200)
```powershell
Invoke-RestMethod -Method Get `
  -Uri "http://localhost:8080/api/settlements/16846548"
```