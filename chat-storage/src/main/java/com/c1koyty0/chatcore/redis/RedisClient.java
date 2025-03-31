package com.c1koyty0.chatcore.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 封装下redis
 *
 * @version 1.0 2025/3/31
 */
@Service
public class RedisClient implements AutoCloseable{
  private static final Logger LOG =  LoggerFactory.getLogger(RedisClient.class);
  @Value("${redis.host}")
  private String host;

  @Value("${redis.port}")
  private String port;

  @Value("${redis.password}")
  private String password;

  // ------------------- 连接池配置 -------------------
  private static final int IDLE_TIME_MS = 30 * 60 *1000;
  private static final int MAX_CONNECTION = 20;
  // ------------------- 连接池配置 -------------------

  private JedisPool pool;

  @PostConstruct
  public void init() {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxIdle(IDLE_TIME_MS);
    poolConfig.setMaxTotal(MAX_CONNECTION);
    pool = new JedisPool(poolConfig, host, Integer.parseInt(port),"", password);
    LOG.info("Jedis pool init. pool config: {}", poolConfig);
  }

  public void set(String key, String value) {
    Jedis client = pool.getResource();
    client.set(key, value);
  }

  // etc

  /**
   * Graceful shutdown
   */
  @Override
  public void close() {
    pool.destroy();
  }
}
