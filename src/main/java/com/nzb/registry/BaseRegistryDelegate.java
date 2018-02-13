package com.nzb.registry;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.nzb.configBean.Registry;

public class BaseRegistryDelegate {

	public static void registry(String ref, ApplicationContext application) {
		Registry registry = application.getBean(Registry.class);
		String protocol = registry.getProtocol();
		@SuppressWarnings("static-access")
		BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
		registryBean.registry(ref, application);
	}

	public static List<String> getRegistry(String id, ApplicationContext application) {
		Registry registry = application.getBean(Registry.class);
		String protocol = registry.getProtocol();
		@SuppressWarnings("static-access")
		BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
		return registryBean.getRegistry(id, application);
	}

}
