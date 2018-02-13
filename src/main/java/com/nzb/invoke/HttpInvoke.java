package com.nzb.invoke;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nzb.configBean.Reference;
import com.nzb.loadbalance.LoadBalance;
import com.nzb.loadbalance.NodeInfo;
import com.nzb.rpc.http.HttpRequest;

public class HttpInvoke implements Invoke {

	public String Invoke(Invocation invocation) throws Exception {
		try {
			Reference reference = invocation.getReference();
			List<String> registryInfo = reference.getRegistryInfo();
			String loadbalance = reference.getLoadbalance();
			LoadBalance loadBalanceBean = reference.getLoadBalance().get(loadbalance);

			NodeInfo nodeInfo = loadBalanceBean.doSelect(registryInfo);

			JSONObject sendparam = new JSONObject();
			sendparam.put("methodName", invocation.getMethod().getName());
			sendparam.put("methodParams", invocation.getObjs());
			sendparam.put("serviceId", reference.getId());
			sendparam.put("paramTypes", invocation.getMethod().getParameterTypes());

			String url = "http://" + nodeInfo.getHost() + ":" + nodeInfo.getPort() + nodeInfo.getContextpath();

			String result = HttpRequest.sendPost(url, sendparam.toJSONString());
			return result;
		} catch (Exception e) {
			throw e;
		}
	}

}
