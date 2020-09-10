package com.shenfeng.yxw.redisson.config;

import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yangxw
 * @Date 10/9/2020 下午7:02
 * @Description
 * @Version 1.0
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    /**
     * 单机
     */
    public RedissonClient getRedisson() {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        return Redisson.create(config);
    }


    /**
     * 哨兵
     *
     * @return
     */
    public RedissonClient getRedisson2() {

        Config config = new Config();
        config.useSentinelServers().addSentinelAddress(
                "redis://172.29.3.245:26378",
                "redis://172.29.3.245:26379",
                "redis://172.29.3.245:26380")
                .setMasterName("mymaster")
                .setPassword("a123456").setDatabase(0);
        return Redisson.create(config);
    }

    /**
     * 集群
     *
     * @return
     */
    public RedissonClient getRedisson3() {

        Config config = new Config();
        config.useClusterServers().addNodeAddress(
                "redis://172.29.3.245:6375",
                "redis://172.29.3.245:6376",
                "redis://172.29.3.245:6377",
                "redis://172.29.3.245:6378",
                "redis://172.29.3.245:6379",
                "redis://172.29.3.245:6380")
                .setPassword("a123456")
                .setScanInterval(5000);
        return Redisson.create(config);
    }


    /**
     * 分布式锁
     */

    public RedissonRedLock getRedissonDistributeLock() {

        Config config1 = new Config();
        config1.useSingleServer().setAddress(
                "redis://172.29.1.180:5378")
                .setPassword("a123456")
                .setDatabase(0);
        RedissonClient redissonClient1 =
                Redisson.create(config1);
        Config config2 = new Config();
        config2.useSingleServer()
                .setAddress("redis://172.29.1.180:5379")
                .setPassword("a123456")
                .setDatabase(0);

        RedissonClient redissonClient2 =
                Redisson.create(config2);
        Config config3 = new Config();
        config3.useSingleServer()
                .setAddress("redis://172.29.1.180:5380")
                .setPassword("a123456")
                .setDatabase(0);
        RedissonClient redissonClient3 = Redisson.create(config3);
        String resourceName = "REDLOCK";

        RLock lock1 = redissonClient1.getLock(resourceName);

        RLock lock2 = redissonClient2.getLock(resourceName);

        RLock lock3 = redissonClient3.getLock(resourceName);

        RedissonRedLock redLock = new RedissonRedLock
                (lock1, lock2, lock3);
//        lock.lock();
//try {
//    System.out.println("获取锁成功，实现业务逻辑");
//    Thread.sleep(10000);
//} catch (InterruptedException e) {
//    e.printStackTrace();
//} finally {
//    lock.unlock();
//}

        return redLock;
    }


}
