package com.nzb.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ReferenceBeanDifinitionParse implements BeanDefinitionParser {

	private Class<?> beanClass;

	public ReferenceBeanDifinitionParse(Class<?> beanClass) {
		super();
		this.beanClass = beanClass;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String id = element.getAttribute("id");
		String intf = element.getAttribute("interface");
		String protocol = element.getAttribute("protocol");
		String loadbalance = element.getAttribute("loadbalance");
		String cluster = element.getAttribute("cluster");
		String retries = element.getAttribute("retries");
		if (id == null || "".equals(id)) {
			throw new RuntimeException("Reference id can not be null");
		}
		if (intf == null || "".equals(intf)) {
			throw new RuntimeException("Reference interface can not be null");
		}
		if (protocol == null || "".equals(protocol)) {
			throw new RuntimeException("Reference protocol can not be null");
		}
		if (loadbalance == null || "".equals(loadbalance)) {
			throw new RuntimeException("Reference loadbalance can not be null");
		}

		beanDefinition.getPropertyValues().add("id", id);
		beanDefinition.getPropertyValues().add("intf", intf);
		beanDefinition.getPropertyValues().add("protocol", protocol);
		beanDefinition.getPropertyValues().add("loadbalance", loadbalance);
		beanDefinition.getPropertyValues().add("retries", retries);
		beanDefinition.getPropertyValues().add("cluster", cluster);
		parserContext.getRegistry().registerBeanDefinition("Reference" + id, beanDefinition);
		return beanDefinition;
	}
}
