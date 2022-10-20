-- wrk -c 500 -t 10 -d 30s -s ./create-order.lua --latency http://localhost:8080/orders

country = "AR"

customerIds = {
    "57a98d98e4b00679b4a830b2",
    "5a934e000102030405000003",
    "5a934e000102030405000010"
}
addressPostcodes = {
    "C1419DVM",
    "4700",
    "H3500ALD"
}
cardNumbers = {
    "4539820506340218",
    "374301540174281",
    "6011789971225778"
}
cartIds = {
    "5a934e000102030405000028",
    "5a934e000102030405000034",
    "5a934e000102030405000048"
}

getBody = function(index)
    return "{\"customerId\": \""..customerIds[index].."\",\"addressPostcode\": \""..addressPostcodes[index].."\",\"cardNumber\": \""..cardNumbers[index].."\",\"cartId\": \""..cartIds[index].."\"}"
end

getRequestTraceId = function()
    return string.lower(country).."-"..(os.clock()*100000000000)
end

-- init random
setup = function(thread)
  math.randomseed(os.time())
end

request = function()
 local path = "/orders"
 local body = getBody(math.random(1, 3))
 wrk.method = "POST"
 wrk.headers["Content-Type"] = "application/json"
 wrk.headers["country"] = country
 wrk.headers["requestTraceId"] = getRequestTraceId()

 return wrk.format("POST", path, wrk.headers, body)
end

done = function(summary, latency, requests)
 io.write("------------------------------\n")
 for _, p in pairs({50, 90, 99, 99.999}) do
  n = latency:percentile(p)
  io.write(string.format("%g%%,%d\n", p, n))
 end
end