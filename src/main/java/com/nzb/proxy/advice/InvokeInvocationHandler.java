package com.nzb.proxy.advice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.nzb.cluster.Cluster;
import com.nzb.configBean.Reference;
import com.nzb.invoke.Invocation;
import com.nzb.invoke.Invoke;

public class InvokeInvocationHandler implements InvocationHandler {
	private Invoke invoke;
	private Reference reference;

	public InvokeInvocationHandler(Invoke invoke, Reference reference) {
		super();
		this.invoke = invoke;
		this.reference = reference;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("already get provider instance, call InvokeInvocationHandler.invoke");
		Invocation invocation = new Invocation();
		invocation.setMethod(method);
		invocation.setObjs(args);
		invocation.setReference(reference);
		invocation.setInvoke(invoke);

		@SuppressWarnings("static-access")
		Cluster cluster = reference.getClusters().get(reference.getCluster());
		String result = cluster.invoke(invocation);
		return result;
	}

}
