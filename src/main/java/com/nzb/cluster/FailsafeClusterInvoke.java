package com.nzb.cluster;

import com.nzb.invoke.Invocation;
import com.nzb.invoke.Invoke;

public class FailsafeClusterInvoke implements Cluster {

	public String invoke(Invocation invocation) throws Exception {
		Invoke invoke = invocation.getInvoke();

		try {
			return invoke.Invoke(invocation);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
