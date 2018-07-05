package com.jxyd.wx.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;


/**
 * 在应用服务器运行时, 从应用容器中获取被spring管理的类的实例; 单元测试时, 会单例地创建spring容器, 从spring容器中获取
 * 
 * @author sun.jun
 */
public class ApplicationContextUtil {

	private static ApplicationContext context;

	/**
	 * 获取对象
	 * 
	 * @param requiredType
	 * @return
	 */
	public static <T> T getBean(Class<T> requiredType) {
		try {
			return getApplicationContext().getBean(requiredType);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取对象
	 *
	 * @return
	 */
	public static Object getBean(String name) {
		try {
			return getApplicationContext().getBean(name);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取容器
	 * 
	 * @return ApplicationContext
	 */
	private static ApplicationContext getApplicationContext() {
		WebApplicationContext wac = ContextLoaderListener.getCurrentWebApplicationContext();
		if (wac != null) {
			return wac;
		} else if (context != null) {
			return context;
		} else {
			initApplicationContext();
			return context;
		}
	}

	/**
	 * 初始化容器
	 * 
	 */
	private static synchronized void initApplicationContext() {
		if (null == context) {
			String[] contextPath = {"classpath*:applicationContext.xml","classpath*:spring*.xml"};
			context = new ClassPathXmlApplicationContext(contextPath);
		}
	}
}
