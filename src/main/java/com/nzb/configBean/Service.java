package com.nzb.configBean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Service extends BaseConfigBean implements InitializingBean, ApplicationContextAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 354657343234563443L;

	private String intf;
	private String ref;
	private String protocol;
	private static ApplicationContext application;

	public String getIntf() {
		return intf;
	}

	public void setIntf(String intf) {
		this.intf = intf;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.application = applicationContext;
	}

	public static ApplicationContext getApplication() {
		return application;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// BaseRegistryDeletegate
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
