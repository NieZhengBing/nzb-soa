package com.nzb.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nzb.invoke.Invocation;
import com.nzb.invoke.Invoke;

public class FailfastClusterInvoke implements Cluster {

	private Logger logger = LoggerFactory.getLogger(FailfastClusterInvoke.class);

	public String invoke(Invocation invocation) throws Exception {
		Invoke invoke = invocation.getInvoke();
		try {
			return invoke.Invoke(invocation);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

}
