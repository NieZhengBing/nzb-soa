package com.nzb.redis;

import redis.clients.jedis.JedisPubSub;

public class RedisServerRegistry extends JedisPubSub {

	@Override
	public void onMessage(String channel, String message) {
		super.onMessage(channel, message);
	}

	@Override
	public void subscribe(String... channels) {
		super.subscribe(channels);
	}

}
