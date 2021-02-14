#!/bin/bash

read -s -p "User: " user
read -s -p "Password: " password


json=$(curl -X POST --location "http://localhost:8080/auth/realms/todolist/protocol/openid-connect/token" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "client_id=todolist&username=$user&password=$password&grant_type=password" | jq -r '.access_token')

echo $json

echo $json | clip
