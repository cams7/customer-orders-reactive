wrk.method = "POST"
wrk.body = "{\"customerUrl\": \"http://localhost:3002/customers/068fb99e-290a-4b0c-ba55-fb8f0239eed8\",\"addressUrl\": \"http://localhost:3002/addresses/1903e7e5-5cca-4cef-9d58-7bc42a2453d7\",\"cardUrl\": \"http://localhost:3002/cards/42bacbfb-452d-45cd-932e-83a7a2b449ab\",\"itemsUrl\": \"http://localhost:3003/carts/f716e351-5df1-4c9c-8324-bd12866e2900/items\"}"
wrk.headers["Content-Type"] = "application/json"
wrk.headers["country"] = "AR"
wrk.headers["requestTraceId"] = "123AR"