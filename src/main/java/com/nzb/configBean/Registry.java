package com.nzb.configBean;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.nzb.registry.BaseRegistry;
import com.nzb.registry.RedisRegistry;

public class Registry extends BaseConfigBean implements InitializingBean, ApplicationContextAware {

	private static final long serialVersionUID = 8052163660420569108L;

	public ApplicationContext application;
	private static Map<String, BaseRegistry> registryMap = new HashMap<String, BaseRegistry>();

	static {
		registryMap.put("redis", new RedisRegistry());
	}

	private String protocol;

	private String address;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.application = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public static Map<String, BaseRegistry> getRegistryMap() {
		return registryMap;
	}

	public static void setRegistryMap(Map<String, BaseRegistry> registryMap) {
		Registry.registryMap = registryMap;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
