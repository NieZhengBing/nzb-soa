package com.nzb.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisApi {
	private static JedisPool pool;
	private static Properties prop = null;
	private static JedisPoolConfig config = null;

	static {
		InputStream in = RedisApi.class.getClassLoader().getResourceAsStream("com/nzb/redis/redis.prooperties");

		prop = new Properties();

		try {
			prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		config = new JedisPoolConfig();
		config.setMaxTotal(Integer.valueOf(prop.getProperty("MAX_TOTAL")));
		config.setMaxIdle(Integer.valueOf(prop.getProperty("MAX_IDLE")));
		config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("MAX_WAIT_MILLS")));
		config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("TEST_ON_BORROW")));
		config.setTestOnReturn(Boolean.valueOf(prop.getProperty("TEST_ON_RETURN")));
		config.setTestWhileIdle(Boolean.valueOf(prop.getProperty("TEST_WHILE_IDLE")));
	}

	public static void createJedisPool(String address) {
		pool = new JedisPool(config, address.split(":")[0], Integer.valueOf(address.split(":")[1]), 100000);
	}

	public static JedisPool getPool() {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(Integer.valueOf(prop.getProperty("MAX_TOTAL")));
			config.setMaxIdle(Integer.valueOf(prop.getProperty("MAX_IDLE")));
			config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("MAX_WAIT_MILLS")));
			config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("TEST_ON_BORROW")));
			config.setTestOnReturn(Boolean.valueOf(prop.getProperty("TEST_ON_RETURN")));
			config.setTestWhileIdle(Boolean.valueOf(prop.getProperty("TEST_WHILE_IDLE")));
			pool = new JedisPool(config, prop.getProperty("REDIS_IP"), Integer.valueOf(prop.getProperty("REDIS_PORT")));
		}
		return pool;
	}

	@SuppressWarnings("deprecation")
	public static void returnResource(JedisPool pool, Jedis jedis) {
		if (jedis != null) {
			pool.returnResource(jedis);
		}
	}

	public static void publish(String channel, String msg) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.publish(channel, msg);
		} catch (Exception e) {

		} finally {
			returnResource(pool, jedis);
		}
	}

	public static boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
		} finally {
			returnResource(pool, jedis);
		}
		return false;
	}

	public static List<String> lrange(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lrange(key, 0, -1);
		} catch (Exception e) {
		} finally {
			returnResource(pool, jedis);
		}
		return null;
	}

	public static void del(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.del(key);
		} catch (Exception e) {
		}
	}

	public static Long hdel(String key, String key1) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hdel(key, key1);
		} catch (Exception e) {

		} finally {
			returnResource(pool, jedis);
		}
		return null;
	}

	public static String get(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {

		} finally {
			returnResource(pool, jedis);
		}
		return value;
	}

	public static String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.set(key, value);
		} catch (Exception e) {
			return "0";
		} finally {
			returnResource(pool, jedis);
		}
	}

	public static Long lpush(String key, String... value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lpush(key, value);
		} catch (Exception e) {
			return 0L;
		} finally {
			returnResource(pool, jedis);
		}
	}

	@SuppressWarnings("unchecked")
	public static String hmset(String key, @SuppressWarnings("rawtypes") Map map) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hmset(key, map);
		} catch (Exception e) {
			return "0";
		} finally {
			returnResource(pool, jedis);
		}
	}

	public static List<String> hmget(String key, String... strings) {
		Jedis jedis = null;
		@SuppressWarnings("unused")
		String value = null;
		try {
			jedis = pool.getResource();
			return jedis.hmget(key, strings);
		} catch (Exception e) {

		} finally {
			returnResource(pool, jedis);
		}
		return null;
	}

}
