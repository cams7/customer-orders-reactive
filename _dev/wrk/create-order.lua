country = "AR"

customerUrl = "http://localhost:3000"
cartUrl = "http://localhost:3001"

customerIds = {
    "068fb99e-290a-4b0c-ba55-fb8f0239eed8",
    "20d54ed5-c94d-48f7-a912-bd85df84a6b9",
    "ea5a387f-3535-4153-bd36-2e944ccdd459"
}
addressIds = {
    "1903e7e5-5cca-4cef-9d58-7bc42a2453d7",
    "b7cb37d3-0094-483c-88de-b6b6b63c1bc2",
    "4ed6fbca-9c27-4bd8-a998-ef190e88a8c8"
}
cardIds = {
    "42bacbfb-452d-45cd-932e-83a7a2b449ab",
    "aea3692c-e415-4643-b2a2-08e776cd58ea",
    "b4086cf9-f8c7-4d32-a48d-f898424301c8"
}
cartIds = {
    "f716e351-5df1-4c9c-8324-bd12866e2900",
    "691c5af7-c651-4f36-b19e-184d37acc481",
    "2ca11a65-2c4b-4671-b98a-724d31f457aa"
}

math.randomseed(os.clock()*100000000000)

getCustomerUrl = function(customerId)
    return customerUrl.."/customers/"..customerId
end

getAddressUrl = function(addressId)
    return customerUrl.."/addresses/"..addressId
end

getCardUrl = function(cardId)
    return customerUrl.."/cards/"..cardId
end

getItemsUrl = function(cartId)
    return cartUrl.."/carts/"..cartId.."/items"
end

getBody = function()
    index = math.random(1, 3)
    return "{\"customerUrl\": \""..getCustomerUrl(customerIds[index]).."\",\"addressUrl\": \""..getAddressUrl(addressIds[index]).."\",\"cardUrl\": \""..getCardUrl(cardIds[index]).."\",\"itemsUrl\": \""..getItemsUrl(cartIds[index]).."\"}"
end

getRequestTraceId = function()
    return string.lower(country).."-"..math.random(10000, 65000)
end

wrk.method = "POST"
wrk.body = getBody()
wrk.headers["Content-Type"] = "application/json"
wrk.headers["country"] = country
wrk.headers["requestTraceId"] = getRequestTraceId()

