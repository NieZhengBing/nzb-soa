package com.nzb.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class RegistryBeanDefinitionParse implements BeanDefinitionParser {
	private Class<?> beanClass;

	public RegistryBeanDefinitionParse(Class<?> beanClass) {
		super();
		this.beanClass = beanClass;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String protocol = element.getAttribute("protocol");
		String address = element.getAttribute("address");

		if (protocol == null || "".equals(protocol)) {
			throw new RuntimeException("Registry protocol can not be null");
		}

		if (address == null || "".equals(address)) {
			throw new RuntimeException("Registry address can not be null");
		}

		beanDefinition.getPropertyValues().add("protocol", protocol);
		beanDefinition.getPropertyValues().add("address", address);
		parserContext.getRegistry().registerBeanDefinition("Registry" + address, beanDefinition);
		return beanDefinition;
	}

}
