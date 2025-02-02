#!/bin/bash
echo "Creating SQS queue..."

awslocal sqs create-queue --queue-name student-queue

awslocal sqs send-message-batch \
    --queue-url=http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/student-queue \
    --entries='[
                {"Id":"Msg-6", "MessageBody":"{\"id\":\"6\", \"name\":\"Frank\", \"status\":\"A\"}" },
                {"Id":"Msg-7", "MessageBody":"{\"id\":\"7\", \"name\":\"Grace\", \"status\":\"G\"}" },
                {"Id":"Msg-8", "MessageBody":"{\"id\":\"8\", \"name\":\"Helen\", \"status\":\"I\"}" },
                {"Id":"Msg-9", "MessageBody":"{\"id\":\"9\", \"name\":\"Ivan\", \"status\":\"E\"}" },
                {"Id":"Msg-10", "MessageBody":"{\"id\":\"10\", \"name\":\"Jack\", \"status\":\"A\"}" }
              ]'

echo "SQS queue created."