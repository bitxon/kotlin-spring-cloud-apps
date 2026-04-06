# Spring Cloud AWS

## Info
[Spring Cloud AWS](https://awspring.io/)\
[Floci](https://github.com/hectorvent/floci)

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

1. DynamoDB Save & Read
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
```shell
aws dynamodb list-tables --no-sign-request --endpoint-url=http://localhost:4566 --region=us-east-1
aws s3api list-buckets --no-sign-request --endpoint-url=http://localhost:4566 --region=us-east-1
aws sqs list-queues --no-sign-request --endpoint-url=http://localhost:4566 --region=us-east-1
```


### DynamoDB
```shell
aws dynamodb put-item \
    --no-sign-request --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --table-name=student  \
    --item='{"id":{"S":"test-id-3"}, "name":{"S":"Mike"}, "status":{"S":"A"}}'
```

### S3
```shell
aws s3api create-bucket --bucket sample-bucket \
    --no-sign-request --endpoint-url=http://localhost:4566 --region=us-east-1
```

```shell
aws s3api put-object \
    --no-sign-request --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --bucket=student-bucket --content-type application/json --key=diplomas-archive.json \
    --body /dev/stdin <<EOF
[
  { "student": "Alice", "year": 2001},
  { "student": "Bob", "year": 2002 }
]
EOF
```

```shell
aws s3api get-object \
    --no-sign-request --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --bucket=student-bucket --key=diplomas-archive.json /dev/stdout
```

#### SQS
```shell
aws sqs send-message \
    --no-sign-request --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --queue-url=http://localhost:4566/000000000000/student-queue \
    --message-body='{"id": "test-id-3","name": "Bob","status": "A"}'
```

```shell
aws sqs send-message-batch \
    --no-sign-request --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --queue-url=http://localhost:4566/000000000000/student-queue \
    --entries='[
                {"Id":"Msg-11", "MessageBody":"{\"id\":\"11\", \"name\":\"Alice\", \"status\":\"A\"}" },
                {"Id":"Msg-12", "MessageBody":"{\"id\":\"12\", \"name\":\"Bob\", \"status\":\"A\"}" },
                {"Id":"Msg-13", "MessageBody":"{\"id\":\"13\", \"name\":\"Charlie\", \"status\":\"D\"}" },
                {"Id":"Msg-14", "MessageBody":"{\"id\":\"14\", \"name\":\"David\", \"status\":\"A\"}" },
                {"Id":"Msg-15", "MessageBody":"{\"id\":\"15\", \"name\":\"Eve\", \"status\":\"D\"}" }
              ]'
```
