package com.nzb.configBean;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.nzb.cluster.Cluster;
import com.nzb.cluster.FailfastClusterInvoke;
import com.nzb.cluster.FailoverClusterInvoke;
import com.nzb.cluster.FailsafeClusterInvoke;
import com.nzb.invoke.HttpInvoke;
import com.nzb.invoke.Invoke;
import com.nzb.invoke.NettyInvoke;
import com.nzb.invoke.RmiInvoke;
import com.nzb.loadbalance.LoadBalance;
import com.nzb.loadbalance.RandomLoadBalance;
import com.nzb.loadbalance.RoundRobinLoadBalance;
import com.nzb.proxy.advice.InvokeInvocationHandler;

@SuppressWarnings("rawtypes")
public class Reference extends BaseConfigBean implements FactoryBean, InitializingBean, ApplicationContextAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3234567323577L;

	private String intf;

	private String loadbalance;

	private String protocol;

	private String cluster;

	private String retries;

	private static ApplicationContext application;

	private Invoke invoke;

	private static Map<String, Invoke> invokes = new HashMap<String, Invoke>();

	private static Map<String, LoadBalance> loadBalances = new HashMap<String, LoadBalance>();

	private static Map<String, Cluster> clusters = new HashMap<String, Cluster>();

	private List<String> registryInfo = new ArrayList<String>();

	static {
		invokes.put("http", new HttpInvoke());
		invokes.put("rmi", new RmiInvoke());
		invokes.put("netty", new NettyInvoke());

		loadBalances.put("random", new RandomLoadBalance());
		loadBalances.put("roundrob", new RoundRobinLoadBalance());

		clusters.put("failover", new FailoverClusterInvoke());
		clusters.put("failfast", new FailfastClusterInvoke());
		clusters.put("failsafe", new FailsafeClusterInvoke());
	}

	public List<String> getRegistryInfo() {
		return registryInfo;
	}

	public void setRegistryInfo(List<String> registryInfo) {
		this.registryInfo = registryInfo;
	}

	public Reference() {
		super();
		System.out.println("Reference constructor");
	}

	public String getIntf() {
		return intf;
	}

	public void setIntf(String intf) {
		this.intf = intf;
	}

	public String getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(String loadbalance) {
		this.loadbalance = loadbalance;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Object getObject() throws IllegalArgumentException, ClassNotFoundException {
		System.out.println("Reference! getObject");
		if (protocol != null && !"".equals(protocol)) {
			invoke = invokes.get(protocol);
		} else {
			Protocol pro = application.getBean(Protocol.class);
			if (pro != null) {
				invoke = invokes.get(pro.getName());
			} else {
				invoke = invokes.get("http");
			}
		}
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] { Class.forName(intf) },
				new InvokeInvocationHandler(invoke, this));
	}

	public Class getObjectType() {
		try {
			if (intf != null && !"".equals(intf)) {
				return Class.forName(intf);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isSingleton() {
		return true;
	}

	public static ApplicationContext getApplication() {
		return application;
	}

	public static void setApplication(ApplicationContext application) {
		Reference.application = application;
	}

	public void afterPropertiesSet() throws Exception {
		// registryInfo = BaseRegistryDelegate.getRegistry(id, application);
		// System.out.println(registryInfo);
		// RedisApi.subscribe("channel" + id, new RedisServerRegistry());

	}

	public static Map<String, LoadBalance> getLoadBalance() {
		return loadBalances;
	}

	public static void setLoadBalance(Map<String, LoadBalance> loadBalances) {
		Reference.loadBalances = loadBalances;
	}

	public String getRetries() {
		return retries;
	}

	public void setRetries(String retries) {
		this.retries = retries;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public static Map<String, Cluster> getClusters() {
		return clusters;
	}

	public static void setClusters(Map<String, Cluster> clusters) {
		Reference.clusters = clusters;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Reference.application = applicationContext;
	}
}
