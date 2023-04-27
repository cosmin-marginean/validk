#!/usr/bin/env bash

curl -XPOST -H "Content-Type: application/json" \
    -d '{"name":"john smith", "email": "test", "age": 12}' \
    http://localhost:8080/users/123/update \
    | jq

#    http://localhost:8080/users/create \

