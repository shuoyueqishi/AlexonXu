local key = KEYS[1]
local expireTimeSec = tonumber(ARGV[1])
local fieldCount = tonumber(ARGV[2])
local fieldKeyIdx = 2
local fieldValIdx = 3

for i=1, fieldCount, 1 do
    redis.pcall('HSET',key,KEYS[fieldKeyIdx],ARGV[fieldValIdx])
    fieldKeyIdx = fieldKeyIdx + 1
    fieldValIdx = fieldValIdx + 1
end
redis.pcall('EXPIER',key,expireTimeSec)