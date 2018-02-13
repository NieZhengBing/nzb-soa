package com.nzb.cluster;

import com.nzb.invoke.Invocation;

public interface Cluster {
	public String invoke(Invocation invocation) throws Exception;

}
