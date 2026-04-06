# Spring Cloud GCP

## Start

1. Start Infrastructure
    ```shell
    docker compose up -d
    ```
2. Run Application
    ```shell
    gradle bootRun
    ```
---

## Test Application

1. Firestore Save & Read
    ```shell
    curl -sS -X POST 'http://localhost:8080/students' -H "Content-Type: application/json" \
      -d '{"id":"100", "name":"John Doe", "status":"A"}' | jq
    ```
    ```shell
    curl -sS -X GET 'http://localhost:8080/students/100' | jq
    ```

2. Storage Save & Read
    ```shell
    curl -sS -X POST 'http://localhost:8080/diplomas' -H "Content-Type: application/json" \
      -d '[{"student":"John Doe 1", "year":2001}, {"student":"John Doe 2", "year":2002}]' | jq
    ```
    ```shell
    curl -sS -X GET 'http://localhost:8080/diplomas' | jq
    ```
---

## Test Infrastructure

### FireStore
```shell
curl -sS -X GET "http://localhost:8091/v1/projects/null/databases/(default)/documents/students"
```
Note: the REST API only shows REST-written data, not gRPC-written data. (This is a Firestore emulator limitation)
Application uses gRPC and that data is invisible to the REST listDocuments endpoint

### PubSub
```shell
curl -sS -X PUT "http://localhost:8092/v1/projects/your-project-id/topics/student-updates" | jq
curl -sS -X PUT "http://localhost:8092/v1/projects/your-project-id/subscriptions/student-updates-subscription" \
  -H "Content-Type: application/json" \
  --data '{ "topic": "projects/your-project-id/topics/student-updates" }' | jq
```
```shell
curl -sS -X POST "http://localhost:8092/v1/projects/your-project-id/topics/student-updates:publish" \
  -H "Content-Type: application/json" \
  --data '{
    "messages": [
      { "data": "'$(echo -n '{"id":"6", "name":"Frank", "status":"A"}' | base64)'" },
      { "data": "'$(echo -n '{"id":"7", "name":"Grace", "status":"G"}' | base64)'" },
      { "data": "'$(echo -n '{"id":"8", "name":"Helen", "status":"I"}' | base64)'" },
      { "data": "'$(echo -n '{"id":"9", "name":"Ivan", "status":"E"}' | base64)'" },
      { "data": "'$(echo -n '{"id":"10", "name":"Jack", "status":"A"}' | base64)'" }
    ]
  }' | jq
```
```shell
curl -sS -X POST "http://localhost:8092/v1/projects/your-project-id/subscriptions/student-updates-subscription:pull" \
  -H "Content-Type: application/json" \
  --data '{ "maxMessages": 10 }' | jq '.receivedMessages[] | { data: (.message.data | @base64d), publishTime: .message.publishTime }'
```

### Storage
```shell
curl -sS -X http://localhost:8093/storage/v1/b | jq
```
```shell
curl -sS -X http://localhost:8093/storage/v1/b/student-bucket/o | jq
```
