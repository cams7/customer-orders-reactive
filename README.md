# Step by step to execute Customer Orders Application that uses reactive programing

This tutorial was tested on a machine with the Ubuntu 18.04.6 LTS operating system. To follow this tutorial, you need the following tools: `git`, `docker`, `node` and `java 11 or higher`.

>If `git` isn't installed, install it through the [Installing Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) tutorial.

>If `docker` isn't installed, install it through the [Install Docker Engine](https://docs.docker.com/engine/install/) tutorial.

>If `node` isn't installed, install it through the [How to Install Node.js and npm on Ubuntu 18.04](https://linuxize.com/post/how-to-install-node-js-on-ubuntu-18.04) tutorial.

>If `java 11` isn't installed, install it through the [Installing OpenJDK 11 (Java Development Kit) on Ubuntu 18.04](https://www.linode.com/docs/guides/how-to-install-openjdk-on-ubuntu-18-04) tutorial.

To make sure the required tools are installed, run the following commands:
```bash
git version
docker version
node --version
npm --version
java --version
javac --version
```

__1__. If `json-server` has not yet been installed, install it by running the following commands:
```bash
npm install -g json-server && json-server --version
```

__2__. If the __customer-orders-reactive__ repository has not yet been cloned, clone it by running the following commands:
```bash
cd YOUR_PATH
git clone https://github.com/cams7/customer-orders-reactive.git
```

__3__. Initialize mongodb by running the following commands:
```bash
cd YOUR_PATH/customer-orders-reactive
docker run -d --rm --name db -p 27017:27017 -v ${PWD}/_dev/scripts/mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js:ro mongo:5.0.9
```

__4__. Open a new terminal tab and start a fake app that has the customer data, customer's address data and customer's card data by running the following commands:
```bash
cd YOUR_PATH/customer-orders-reactive
json-server --watch ${PWD}/_dev/json-server/customers.json --port 8084 --host 0.0.0.0 --middlewares ${PWD}/_dev/json-server/customers.js
```

__5__. Open a new terminal tab and start a fake app that has the customer's cart data by running the following commands:
```bash
cd YOUR_PATH/customer-orders-reactive
json-server --watch ${PWD}/_dev/json-server/carts.json --port 8085 --host 0.0.0.0 --middlewares ${PWD}/_dev/json-server/carts.js
```

__6__. Open a new terminal tab and start a fake app used to verify payment by running the following commands:
```bash
cd YOUR_PATH/customer-orders-reactive
java -jar ${PWD}/_dev/payment.jar --server.port=8082
```

__7__. Open a new terminal tab and start a fake app used to create shipping by running the following commands:
```bash
cd YOUR_PATH/customer-orders-reactive
java -jar ${PWD}/_dev/shipping.jar --server.port=8083
```

__8__. Open a new terminal tab and start customer-orders app inside a new docker container by running the following commands:
```bash
cd YOUR_PATH/customer-orders-reactive
docker run -it --rm --net host -v ${PWD}:/work -v ${HOME}/.m2:/root/.m2 -e BUILDER_ADD_CLIENT_CONNECTOR=true -w /work --memory="1g" adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.14.1_1 sh

/work/mvnw clean verify -f /work
java -jar /work/target/customer-orders-reactive-0.0.1-SNAPSHOT.jar --server.port=8081
```

__9__. Open a new terminal tab and run the stress tests inside a new docker container by running the following commands:
```bash
cd YOUR_PATH/customer-orders-reactive
docker run -it --rm --net host -v ${PWD}:/work -w /work alpine:3.16.2 sh

apk update && apk add --no-cache coreutils curl jq tmux wrk

curl 'http://localhost:8084/customers/57a98d98e4b00679b4a830b2' | jq
curl 'http://localhost:8084/addresses?customerId=57a98d98e4b00679b4a830b2&postcode=C1419DVM' | jq
curl 'http://localhost:8084/cards?/customerId=57a98d98e4b00679b4a830b2&longNum=4539820506340218' | jq
curl 'http://localhost:8085/items?customerId=57a98d98e4b00679b4a830b2&cartId=5a934e000102030405000028' | jq

curl 'http://localhost:8081/orders' -H 'country: BR' -H 'requestTraceId: 123BR' | jq
curl 'http://localhost:8081/orders/5a934e000102030405000000' -H 'country: BR' -H 'requestTraceId: 123BR' | jq
curl -X DELETE 'http://localhost:8081/orders/5a934e000102030405000000' -H 'country: BR' -H 'requestTraceId: 123BR'

curl -X POST 'http://localhost:8081/orders' -H 'Content-Type: application/json' -H 'country: AR' -H 'requestTraceId: 123AR' -d '{"customerId": "57a98d98e4b00679b4a830b2","addressPostcode": "C1419DVM","cardNumber": "4539820506340218","cartId": "5a934e000102030405000028"}' | jq

wrk -d1m -t10 -c500 -s /work/_dev/wrk/create-order.lua --latency http://localhost:8081/orders
```

__10__. If you have some MongoDB  Client, after running the stress tests, run the following commands to verify the integrity of the registered data:

	db.getCollection('AR-orders').count({})

	db.getCollection('AR-orders').count({
 	  $and:[
  	    {'customer.customerId': '57a98d98e4b00679b4a830b2'},
  	    {'address.addressId': '57a98d98e4b00679b4a830b0'},
  	    {'card.cardId': '57a98d98e4b00679b4a830b1'},
  	    {items:{$elemMatch:{productId: '9aff4cc5-f921-4157-8976-41ceae93ae54', "quantity": 1}}},
  	    {items:{$elemMatch:{productId: '2bd4204f-26f5-43c0-81c0-ba61230d6131', "quantity": 2}}}
 	  ]
	})

	db.getCollection('AR-orders').count({
 	  $and:[
  	    {'customer.customerId': '5a934e000102030405000003'},
  	    {'address.addressId': '57a98ddce4b00679b4a830d1'},
  	    {'card.cardId': '57a98ddce4b00679b4a830d2'},
  	    {items:{$elemMatch:{productId: 'd610629a-e8d8-4d84-b87e-ef518caa66bd', "quantity": 2}}},
  	    {items:{$elemMatch:{productId: '1cd7aef5-de02-44e0-a6d3-633799c91964', "quantity": 3}}}
 	  ]
	})

	db.getCollection('AR-orders').count({
 	  $and:[
  	    {'customer.customerId': '5a934e000102030405000010'},
  	    {'address.addressId': '5a934e000102030405000011'},
  	    {'card.cardId': '5a934e000102030405000012'},
  	    {items:{$elemMatch:{productId: '1b22252e-4291-4501-8908-c5796f1b1d45', "quantity": 1}}},
  	    {items:{$elemMatch:{productId: 'd197efe1-4d0b-40d2-add1-a3a9803e3cee', "quantity": 1}}},
  	    {items:{$elemMatch:{productId: '170c85c8-e4d3-4c55-8c80-9aa7b46d2eb1', "quantity": 4}}},
  	    {items:{$elemMatch:{productId: '2dfd5320-6555-4a90-acf3-dd3fe2617040', "quantity": 2}}},
  	    {items:{$elemMatch:{productId: '66bd0146-ddcf-4b85-8e23-5b181d32ee6c', "quantity": 3}}}
 	  ]
	})

	db.getCollection('AR-orders').count({'registeredShipping': {$ne: true}})

	db.getCollection('shippings').count({country:'AR'})
