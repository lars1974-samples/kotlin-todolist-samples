

docker run -p 8080:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin quay.io/keycloak/keycloak

* login to keycloak
* Up to the right create new realm named todolist and switch to that realm
* create client named todolist and create a callback url http://localhost:8888/*
* add role named user
* create a user eg lars and with email and map it to user role