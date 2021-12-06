# Service Monitoring Tool

REST API monitoring service app with JWT-token based Authentication/Authorization

---

### Summary
1. Service implemented, delivered and working according to the requirements
2. Total time spent approx. ~20...30 hours (can't figure out on exact time, few days in a raw, couple of hours per day)
3. Some extras, like Swagger (not specified, but nice to have) might be added, but haven't because of lack of time
4. Testing (Unit/Integration) is missing, because of lack of time, but can be added as well
5. All endpoints / logic were manually tested/verified to meet the requirements/specification
---

**This repo contains:**
* Spring Boot Web app, with Maven, Lombok, etc.
* Spring Boot Data JPA starter
    * Using MySQL
* Spring Boot Security (JWT-token)
* REST API - controllers/services/repositories/models for:
  * Services (CRUD)
  * Authentication
* Pooling config & logic - to get service's state
---

### Drawbacks
1. Unit / Integration tests are missing (despite everything was manually tested and meet the requirements)
2. Swagger integration for documenting REST API - nice to have, missing, because of lack of the time available
3. Application design/implementation might be improved (I know there is a space for improvements)
4. Primitive front-end, sometimes too basic logic, usage of hardcoded values (environment variables must be used instead)
5. Some extra-features (sorting, filtering, validation) has to be implemented / improved
6. Too much time was focused on security (RBAC, JWT, config, etc.) and approaches I weren't used at the end
7. For temporary use - database schema is updatable, according to configuration setting, to make it easier, faster and more flexible to add and update new / existing entities
---

### Challenges
1. Front-End in general - spent too much time on designing and developing of that part
2. Breaks and context switching during implementing (family, work, etc.)
3. Security / Auth + Docker integration took some time
3. Tried to add swagger and websockets, but finally went on with another approach
4. Spent some time on this document :)
---

### HOWTO
_Prerequisites: Docker (installed and running), Java 11, Git and Maven_

1. Clone repo: https://github.com/Taraskin/monitoring:    
   `git clone https://github.com/Taraskin/monitoring.git`
2. Navigate to project directory and run:
   ```shell
   cd monitoring
   mvn clean install
   ```
   _-_ to verify it is builds
3. Start DB instance (_Dockerized_*)   
4. Run generated JAR-file:
    ```shell
    java -jar target/service-monitoring-0.0.1-SNAPSHOT.jar
    ```
5. You can also run the app from the IDE
6. _Register_ user by using REST endpoint: 
   `POST /auth/register` - _see more details below_
7. Navigate to http://localhost:8080, login with registered user credentials, try to add new service(s) for monitoring 

**_P.S. MySQL in Docker_** * 

`application.yml` already contains connection information to this DB.
```shell script
docker-compose up -d
```
---

### Some main REST API - endpoints
* **Auth** `/api/auth`
    * `POST /auth/register` *- register new user*
        * Payload:
          ```json
          {
          "username": "User_Name",
          "password": "User_Pass",
          "roles": ["ROLE_ADMIN"]
          }
          ```
          _-_ _where the role is one of `ROLE_ADMIN` or `ROLE_USER`_


* `POST /auth/login` *- user login*
    * Payload:
       ```json
       {
       "username": "User_Name",
       "password": "User_Pass"
       }
       ```

**Note** _`ROLE_ADMIN` and `ROLE_USER` automatically added on the app's first run_

**Note** _User's passwords stored in the database hashed / encrypted_: `$2a$10$Rzgjgy9eZ3wJdG4lIAC1r.2PjCaDgqcjtET.R2NIqaaqKRSt5omza`

**Note** _Security JWT tokens (must be present in request header for protected endpoints) have configurable expiration time - see next section in application.yml:_
```yaml
  # Security Configuration
  security:
    jwt:
      secret: SomeSecretKey
      expirationMs: 3600000 # 1000 * 60 * 60 = 3600000 ms (1 hour)
```
_-_ _every expired token has to be refreshed by using `/api/auth/login` endpoint_

---

* **Service** `/api/services` - full **CRUD**
  * `GET /api/services` _- get all services_ 
  * `GET /api/services/{ID}` _- get service by ID_ 
  * `POST /api/services` _add a new service, per user_
      * Payload:
        ```json
        {
         "name": "Service Name",
         "url": "Service URL"
        }
        ```
  * `PUT /api/services` _update service, allowed only for users with `ADMIN` role_
      * Payload:
        ```json
        {
         "id": 42, 
         "name": "Service Name (Updated)",
         "url": "Service URL (Updated)"
        }
        ```
    
  * `DELETE /api/services/{ID}` _delete the service by ID, allowed only owner, with `ADMIN` role_

---