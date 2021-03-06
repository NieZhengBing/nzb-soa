package com.nzb.loadbalance;

import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class RoundRobinLoadBalance implements LoadBalance {

	private static Integer index = 0;

	public NodeInfo doSelect(List<String> registryInfo) {
		synchronized (index) {
			if (index > registryInfo.size()) {
				index = 0;
			}
			String registry = registryInfo.get(index);
			index++;
			JSONObject registryJo = JSONObject.parseObject(registry);
			Collection<Object> values = registryJo.values();
			JSONObject node = new JSONObject();
			for (Object value : values) {
				node = JSONObject.parseObject(value.toString());
			}
			JSONObject protocol = node.getJSONObject("protocol");
			NodeInfo nodeInfo = new NodeInfo();
			nodeInfo.setHost(protocol.get("host") != null ? protocol.getString("host") : "");
			nodeInfo.setPort(protocol.get("port") != null ? protocol.getString("port") : "");
			nodeInfo.setContextpath(protocol.get("contextpath") != null ? protocol.getString("contextpath") : "");
			return nodeInfo;
		}
	}
}
