package com.nzb.invoke;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nzb.configBean.Reference;
import com.nzb.loadbalance.LoadBalance;
import com.nzb.loadbalance.NodeInfo;
import com.nzb.rmi.RmiUtil;
import com.nzb.rmi.SoaRmi;

public class RmiInvoke implements Invoke {

	public String Invoke(Invocation invocation) throws Exception {
		try {
			Reference reference = invocation.getReference();
			List<String> registryInfo = reference.getRegistryInfo();
			String loadbalance = reference.getLoadbalance();
			@SuppressWarnings("static-access")
			LoadBalance loadBalanceBean = reference.getLoadBalance().get(loadbalance);
			NodeInfo nodeInfo = loadBalanceBean.doSelect(registryInfo);

			JSONObject sendparam = new JSONObject();
			sendparam.put("methodName", invocation.getMethod().getName());
			sendparam.put("methodParam", invocation.getObjs());
			sendparam.put("serviceId", reference.getId());
			sendparam.put("paramTypes", invocation.getMethod().getParameterTypes());

			RmiUtil rmi = new RmiUtil();
			SoaRmi soarmi = rmi.startRmiClient(nodeInfo, "nzbsoarmi");
			return soarmi.invoke(sendparam.toJSONString());
		} catch (Exception e) {
			throw e;
		}
	}

}
