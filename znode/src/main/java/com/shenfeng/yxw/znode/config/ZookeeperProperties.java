package com.shenfeng.yxw.znode.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author yangxw
 * @Date 11/9/2020 上午9:16
 * @Description
 * @Version 1.0
 */
@Component
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperProperties {
    private int baseSleepTimeMs;
    private int maxRetries;
    private String connectString;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
}
