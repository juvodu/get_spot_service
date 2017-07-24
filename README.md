# Spot Service

Simple micro service to CRUD surfspots based on an AWS serverless technology stack:
- DynamoDB Spot table for persistence
- Each CRUD part is based on a separate Lambda function
- API Gateway as an entry point, exposing the micro service as an API

Implemented with the serverless framework. 