package com.nzb.configBean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.nzb.netty.NettyUtil;
import com.nzb.rmi.RmiUtil;

public class Protocol extends BaseConfigBean
		implements InitializingBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

	private static final long serialVersionUID = 14546547633545543L;

	private String name;

	private String port;

	private String host;

	private String contextpath;

	private static ApplicationContext application;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getContextpath() {
		return contextpath;
	}

	public void setContextpath(String contextpath) {
		this.contextpath = contextpath;
	}

	public static ApplicationContext getApplication() {
		return application;
	}

	public static void setApplication(ApplicationContext application) {
		Protocol.application = application;
	}

	public void afterPropertiesSet() {
		if (name.equalsIgnoreCase("rmi")) {
			RmiUtil rmi = new RmiUtil();
			rmi.startRmiServer(host, port, "nzbsoarmi");
		}
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!ContextRefreshedEvent.class.getName().equals(event.getClass().getName())) {
			return;
		}
		if (!"netty".equalsIgnoreCase(name)) {
			return;
		}
		new Thread(new Runnable() {
			public void run() {
				try {
					NettyUtil.startServer(port);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub

	}
}
