#!/bin/bash
echo "Creating S3 bucket..."

awslocal s3api create-bucket --bucket student-bucket

echo "S3 bucket created."

