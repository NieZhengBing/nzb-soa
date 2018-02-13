package com.nzb.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ServiceBeanDefinitionParse implements BeanDefinitionParser {

	private Class<?> beanClass;

	public ServiceBeanDefinitionParse(Class<?> beanClass) {
		super();
		this.beanClass = beanClass;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String intf = element.getAttribute("interface");
		String ref = element.getAttribute("ref");
		String protocol = element.getAttribute("protocol");

		if (intf == null || "".equals(intf)) {
			throw new RuntimeException("service intf can not be null");
		}
		if (ref == null || "".equals(ref)) {
			throw new RuntimeException("service ref can not be null");
		}
		beanDefinition.getPropertyValues().add("intf", intf);
		beanDefinition.getPropertyValues().add("ref", ref);
		beanDefinition.getPropertyValues().add("protocol", protocol);
		parserContext.getRegistry().registerBeanDefinition("service" + ref + intf, beanDefinition);
		return beanDefinition;
	}

}
