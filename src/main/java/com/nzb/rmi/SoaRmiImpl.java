package com.nzb.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nzb.configBean.Service;

public class SoaRmiImpl extends UnicastRemoteObject implements SoaRmi {

	protected SoaRmiImpl() throws RemoteException {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 235576893111223441L;

	public String invoke(String param) throws RemoteException {
		JSONObject requestparam = JSONObject.parseObject(param);
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
			Object result;
			try {
				result = method.invoke(serviceBean, objs);
				return result.toString();
			} catch (IllegalAccessException e) {
				// TODO: handle exception
			} catch (IllegalArgumentException e) {
				// TODO: handle exception
			} catch (InvocationTargetException e) {
				// TODO: handle exception
			}
		} else {
			return "----------------nosuchmethod-----------------";
		}
		return null;
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
			}
			if (!isSameType) {
				continue nzb;
			}
		}
		return null;
	}

}
