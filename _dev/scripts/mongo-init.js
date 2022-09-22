db = db.getSiblingDB('customer-orders');

db.createCollection("orders");

db.orders.insertMany([
   {
      "_id": ObjectId("57a98d98e4b00679b4a830af"),
      "customer": {
         "id": "703c327b-8b61-4f32-bf1a-fb3108a6f7e1",
         "fullName": "Gustavo Severino Tomás Ramos",
         "username": "gustavo"
      },
      "address": {
        "id": "a679084a-03e7-49c7-9516-5a7d6757e1c2",
        "number": "956",
        "street": "Rua Manoel Gregório Mattos",
        "postcode": "89816-170",
        "city": "Chapecó",        
        "federativeUnit": "SC",
        "country": "BR"
      },
      "card": {
        "id": "818c8544-dfb1-49b2-8212-eb9dcdbd57c9",
        "longNum": "5413 0962 7910 9654"
      },
      "items": [
        {
          "id": "9aff4cc5-f921-4157-8976-41ceae93ae54",
          "quantity": 3,
          "unitPrice": 18.0
        },
        {
          "id": "e67ef6e3-10f5-42dd-8b98-dd0d793ca2fa",
          "quantity": 2,
          "unitPrice": 35.0
        },
        {
          "id": "2c22ea64-39f1-474b-92d2-b92684dedaa0",
          "quantity": 1,
          "unitPrice": 27.0
        }
      ],
      "registrationDate": ISODate("2022-09-22T17:46:53Z"),
      "total": 161.55
    },
    {
      "_id": ObjectId("57a98d98e4b00679b4a830b2"),
      "customer": {
         "id": "068fb99e-290a-4b0c-ba55-fb8f0239eed8",
         "fullName": "Pedro Danilo André da Cruz",
         "username": "pedro"
      },
      "address": {
        "id": "1903e7e5-5cca-4cef-9d58-7bc42a2453d7",
        "number": "340",
        "street": "Vila Eudócio",
        "postcode": "60526-560",
        "city": "Fortaleza",        
        "federativeUnit": "CE",
        "country": "BR"
      },
      "card": {
        "id": "42bacbfb-452d-45cd-932e-83a7a2b449ab",
        "longNum": "5540 2279 7869 4805"
      },
      "items": [
        {
          "id": "f4d8d296-3ac8-4073-beef-c703cc5030f2",
          "quantity": 2,
          "unitPrice": 9.0
        },
        {
          "id": "6799d4e5-23aa-4da2-b10e-4a09ee053ceb",
          "quantity": 5,
          "unitPrice": 11.5
        },
        {
          "id": "2c22ea64-39f1-474b-92d2-b92684dedaa0",
          "quantity": 1,
          "unitPrice": 27.0
        }
      ],
      "registrationDate": ISODate("2022-09-21T12:30:21Z"),
      "total": 113.05
    },
    {
      "_id": ObjectId("57a98d98e4b00679b4a830b5"),
      "customer": {
         "id": "7329d57a-4028-41cc-9626-a0c41246a623",
         "fullName": "Raimundo José Gael Araújo",
         "username": "raimundo"
      },
      "address": {
        "id": "594fbca6-270c-4299-8b2c-084da56c756c",
        "number": "773",
        "street": "Passagem Santa Maria",
        "postcode": "66625-143",
        "city": "Belém",        
        "federativeUnit": "PA",
        "country": "BR"
      },
      "card": {
        "id": "cfecdbd8-6dd1-45ae-a453-1adc20a6b065",
        "longNum": "4929 3483 5158 1213"
      },
      "items": [
        {
          "id": "9aff4cc5-f921-4157-8976-41ceae93ae54",
          "quantity": 1,
          "unitPrice": 18
        },
        {
          "id": "2bd4204f-26f5-43c0-81c0-ba61230d6131",
          "quantity": 2,
          "unitPrice": 21
        }
      ],
      "registrationDate": ISODate("2022-09-21T16:21:44Z"),
      "total": 70.55
    }
]);
