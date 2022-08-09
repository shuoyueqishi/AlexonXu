local key = KEYS[1]
local buyQty = tonumber(ARGV[1])

if (redis.call('exists', key) == 1) then
    local stock = tonumber(redis.call('get', key))
    if (stock - buyQty < 0) then
        return -1
    end
    redis.call('incrby', key, -buyQty)
    return stock - buyQty
end
return -2