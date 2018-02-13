package com.nzb.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.nzb.configBean.Protocol;
import com.nzb.configBean.Registry;
import com.nzb.configBean.Service;
import com.nzb.redis.RedisApi;

public class RedisRegistry implements BaseRegistry {

	@Override
	public boolean registry(String ref, ApplicationContext application) {
		try {
			Protocol protocol = application.getBean(Protocol.class);
			Map<String, Service> services = application.getBeansOfType(Service.class);

			Registry registry = application.getBean(Registry.class);
			RedisApi.createJedisPool(registry.getAddress());
			for (Map.Entry<String, Service> entry : services.entrySet()) {
				if (entry.getValue().getRef().equals(ref)) {
					JSONObject jo = new JSONObject();
					jo.put("protocol", JSONObject.toJSONString(protocol));
					jo.put("service", JSONObject.toJSONString(entry.getValue()));

					JSONObject ipport = new JSONObject();
					ipport.put(protocol.getHost() + ":" + protocol.getPort(), jo);
					lpush(ipport, ref);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unlikely-arg-type")
	private void lpush(JSONObject ipport, String key) {
		if (RedisApi.exists(key)) {
			Set<String> keys = ipport.keySet();
			@SuppressWarnings("unused")
			String ipportStr = "";
			for (String kk : keys) {
				ipportStr = kk;
			}

			List<String> registryInfo = RedisApi.lrange(key);
			List<String> newRegistry = new ArrayList<String>();

			boolean isold = false;

			for (String node : registryInfo) {
				JSONObject jo = JSONObject.parseObject(node);
				if (jo.containsKey(ipport)) {
					newRegistry.add(ipport.toJSONString());
					isold = true;
				} else {
					newRegistry.add(node);
				}
			}

			if (isold) {
				if (newRegistry.size() > 0) {
					RedisApi.del(key);
					String[] newReStr = new String[newRegistry.size()];
					for (int i = 0; i < newRegistry.size(); i++) {
						newReStr[i] = newRegistry.get(i);
					}
					RedisApi.lpush(key, newReStr);
				}
			} else {
				RedisApi.lpush(key, ipport.toJSONString());
			}
		} else {
			RedisApi.lpush(key, ipport.toJSONString());
		}
	}

	@Override
	public List<String> getRegistry(String id, ApplicationContext application) {
		try {
			Registry registry = application.getBean(Registry.class);
			RedisApi.createJedisPool(registry.getAddress());
			if (RedisApi.exists(id)) {
				return RedisApi.lrange(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
