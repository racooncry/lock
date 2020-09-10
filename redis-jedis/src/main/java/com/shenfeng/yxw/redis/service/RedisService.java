package com.shenfeng.yxw.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author yangxw
 * @Date 10/9/2020 下午6:39
 * @Description
 * @Version 1.0
 */
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 正确做法
     * @param key
     * @param UniqueId
     * @param seconds
     * @return
     */
    public boolean tryLock_with_set(String key, String UniqueId, int seconds) {
        Jedis jedis = jedisPool.getResource();
        return "OK".equals(jedis.set(key, UniqueId, "NX", "EX", seconds));
    }
    public boolean releaseLock_with_lua(String key,String value) {
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                "return redis.call('del',KEYS[1]) else return 0 end";
        Jedis jedis = jedisPool.getResource();
        return jedis.eval(luaScript, Collections.singletonList(key), Collections.singletonList(value)).equals(1L);
    }


    public boolean tryLock(String key, String requset, int timeout) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.setnx(key, requset);
        // result = 1时，设置成功，否则设置失败
        if (result == 1L) {
            return jedis.expire(key, timeout) == 1L;
        } else {
            return false;
        }
    }
    public boolean tryLock_with_lua(String key, String UniqueId, int seconds) {
        Jedis jedis = jedisPool.getResource();
        String lua_scripts = "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then" +
                "redis.call('expire',KEYS[1],ARGV[2]) return 1 else return 0 end";
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        keys.add(key);
        values.add(UniqueId);
        values.add(String.valueOf(seconds));
        Object result = jedis.eval(lua_scripts, keys, values);
        // 判断是否成功
        return result.equals(1L);
    }

}
