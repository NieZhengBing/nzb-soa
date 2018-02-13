package com.nzb.spring.parse;

import javax.naming.Reference;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.nzb.configBean.Protocol;
import com.nzb.configBean.Registry;
import com.nzb.configBean.Service;

public class SOANamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParse(Registry.class));
		registerBeanDefinitionParser("protocol", new RegistryBeanDefinitionParse(Protocol.class));
		registerBeanDefinitionParser("reference", new RegistryBeanDefinitionParse(Reference.class));
		registerBeanDefinitionParser("service", new ServiceBeanDefinitionParse(Service.class));
	}

}
