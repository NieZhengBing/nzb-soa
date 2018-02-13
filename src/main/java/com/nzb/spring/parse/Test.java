package com.nzb.spring.parse;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	public static void name(String... args) {
		ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("mybatis.xml");
	}

}
