package com.nzb.configBean;

import java.io.Serializable;

public class BaseConfigBean implements Serializable {

	private static final long serialVersionUID = 1334352211112L;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
