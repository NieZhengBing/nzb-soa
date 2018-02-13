package com.nzb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
	public static void name(String... args) {
		ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("mybatis.xml");
		UserService userService = app.getBean(UserService.class);
		userService.eat();
	}

}
