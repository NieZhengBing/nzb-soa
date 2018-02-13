package com.nzb.cluster;

import com.nzb.invoke.Invocation;
import com.nzb.invoke.Invoke;

public class FailoverClusterInvoke implements Cluster {

	public String invoke(Invocation invocation) throws Exception {
		String retries = invocation.getReference().getRetries();
		Integer retriint = Integer.parseInt(retries);
		for (int i = 0; i < retriint; i++) {
			try {
				Invoke invoke = invocation.getInvoke();
				return invoke.Invoke(invocation);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		throw new RuntimeException("retiries " + retries + " all failed!!!");
	}

}
