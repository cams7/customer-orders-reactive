country = "AR"

customerUrl = "http://localhost:8084"
cartUrl = "http://localhost:3001"

customerIds = {
    "57a98d98e4b00679b4a830b2",
    "57a98d98e4b00679b4a830b1",
    "5a934e000102030405000010"
}
addressIds = {
    "57a98d98e4b00679b4a830b0",
    "57a98ddce4b00679b4a830d1",
    "5a934e000102030405000011"
}
cardIds = {
    "57a98d98e4b00679b4a830b1",
    "57a98ddce4b00679b4a830d2",
    "5a934e000102030405000012"
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

