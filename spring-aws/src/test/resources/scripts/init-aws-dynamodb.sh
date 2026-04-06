#!/bin/sh
export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_ENDPOINT_URL=http://localhost:4566

echo "Creating DynamoDB tables..."

aws dynamodb create-table \
   --table-name student \
   --attribute-definitions AttributeName=id,AttributeType=S \
   --key-schema AttributeName=id,KeyType=HASH \
   --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

aws dynamodb put-item --table-name=student --item='{"id":{"S":"1"}, "name":{"S":"Alice"}, "status":{"S":"A"}}'
aws dynamodb put-item --table-name=student --item='{"id":{"S":"2"}, "name":{"S":"Bob"}, "status":{"S":"G"}}'
aws dynamodb put-item --table-name=student --item='{"id":{"S":"3"}, "name":{"S":"Charlie"}, "status":{"S":"I"}}'
aws dynamodb put-item --table-name=student --item='{"id":{"S":"4"}, "name":{"S":"David"}, "status":{"S":"E"}}'
aws dynamodb put-item --table-name=student --item='{"id":{"S":"5"}, "name":{"S":"Eve"}, "status":{"S":"A"}}'

echo "DynamoDB tables created."
