package com.nzb.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ProtocolBeanDefinitionParse implements BeanDefinitionParser {
	private Class<?> beanClass;

	public ProtocolBeanDefinitionParse(Class<?> beanClass) {
		super();
		this.beanClass = beanClass;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String name = element.getAttribute("name");
		String host = element.getAttribute("host");
		String port = element.getAttribute("port");
		String contextpath = element.getAttribute("contextpath");

		if (name == null || "".equals(name)) {
			throw new RuntimeException("protocol name is not null");
		}
		if (host == null || "".equals(host)) {
			throw new RuntimeException("protocol host can not be null");
		}
		if (port == null | "".equals(port)) {
			throw new RuntimeException("protocol port can not be null");
		}

		beanDefinition.getPropertyValues().addPropertyValue("name", name);
		beanDefinition.getPropertyValues().addPropertyValue("host", host);
		beanDefinition.getPropertyValues().addPropertyValue("port", port);
		beanDefinition.getPropertyValues().addPropertyValue("contextpath", contextpath);
		parserContext.getRegistry().registerBeanDefinition("protocol" + host + port, beanDefinition);

		return beanDefinition;
	}

}
