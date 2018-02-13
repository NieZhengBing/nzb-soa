package com.nzb.remote.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nzb.configBean.Service;

public class DispatcherServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 145546588974443L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			JSONObject requestparam = httpProcess(req, resp);
			String serviceId = requestparam.getString("serviceId");
			String methodName = requestparam.getString("methodName");
			JSONArray paramTypes = requestparam.getJSONArray("paramTypes");
			JSONArray methodParamJa = requestparam.getJSONArray("methodParams");
			Object[] objs = null;
			if (methodParamJa != null) {
				objs = new Object[methodParamJa.size()];
				int i = 0;
				for (Object o : methodParamJa) {
					objs[i++] = o;
				}
			}
			ApplicationContext application = Service.getApplication();
			Object serviceBean = application.getBean(serviceId);

			Method method = getMethod(serviceBean, methodName, paramTypes);
			if (method != null) {
				Object result = null;
				result = method.invoke(serviceBean, objs);

				PrintWriter pw = resp.getWriter();
				pw.write(result.toString());
			} else {
				PrintWriter pw = resp.getWriter();
				pw.write("-----------------nosuchmethod----------------------");
			}
		} catch (IllegalAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private Method getMethod(Object bean, String methodName, JSONArray paramTypes) {
		Method[] methods = bean.getClass().getMethods();
		List<Method> retMethod = new ArrayList<Method>();
		for (Method method : methods) {
			if (methodName.trim().equals(method.getName())) {
				retMethod.add(method);
			}
		}
		if (retMethod.size() == 1) {
			return retMethod.get(0);
		}

		boolean isSameSize = false;
		boolean isSameType = false;
		nzb: for (Method method : retMethod) {
			Class<?>[] types = method.getParameterTypes();
			if (types.length == paramTypes.size()) {
				isSameSize = true;
			}
			if (!isSameSize) {
				continue;
			}
			for (int i = 0; i < types.length; i++) {
				if (types[i].toString().contains(paramTypes.getString(i))) {
					isSameType = true;
				} else {
					isSameType = false;
				}
				if (!isSameType) {
					continue nzb;
				}
			}
			if (isSameType) {
				return method;
			}
		}
		return null;
	}

	public static JSONObject httpProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		StringBuffer sb = new StringBuffer();
		ServletInputStream is = req.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String s = "s";
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		if (sb.toString().length() < 0) {
			return null;
		} else {
			return JSONObject.parseObject(sb.toString());
		}
	}

}
