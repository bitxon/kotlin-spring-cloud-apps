
# Spring Cloud Azure

# Run Application

1. Start Infrastructure
    ```shell
    docker-compose up -d
    ```

2. Extract certificates (Only for HTTPS connection)
    ```shell
    mkdir -p .temp
    docker cp cosmosdb-emulator:/scripts/certs/domain.crt .temp/domain.crt
    docker cp cosmosdb-emulator:/scripts/certs/rootCA.crt .temp/rootCA.crt
    ```

3. Save to Java truststore (Only for HTTPS connection)
    ```shell
    keytool -delete -alias cosmosdb-root -cacerts -storepass changeit -noprompt
    keytool -importcert -alias cosmosdb-root -file .temp/rootCA.crt -trustcacerts -cacerts -storepass changeit -noprompt
    keytool -delete -alias cosmosdb-domain -cacerts -storepass changeit -noprompt
    keytool -importcert -alias cosmosdb-domain -file .temp/domain.crt -trustcacerts -cacerts -storepass changeit -noprompt
    ```

4. Run
    ```shell
    gradle bootRun
    ```

---
## Test Application

1. CosmosDb Save & Read
    ```shell
    curl -X POST 'http://localhost:8080/students' -H "Content-Type: application/json" \
      -d '{"id":"100", "name":"John Doe", "status":"A"}' | jq
    ```
    ```shell
    curl -X GET 'http://localhost:8080/students/100' | jq
    ```

2. Storage Save & Read
    ```shell
    curl -X POST 'http://localhost:8080/diplomas' -H "Content-Type: application/json" \
      -d '[{"student":"John Doe 1", "year":2001}, {"student":"John Doe 2", "year":2002}]' | jq
    ```
    ```shell
    curl -X GET 'http://localhost:8080/diplomas' | jq
    ```

---
## Azure


### CosmosDb

### Storage

#### EventHub
