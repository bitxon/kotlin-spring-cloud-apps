# Spring Cloud GCP

# Run Application

1. Start Infrastructure
    ```shell
    docker-compose up -d
    ```

2. Run
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
## GCP

### FireStore
```shell
curl -X GET "http://localhost:8091/v1/projects/null/databases/(default)/documents/students"
```
