package com.inno72.util;

import java.util.Map;

public class MapUtil {
	@SuppressWarnings("unchecked")
	public static <T> T getParam(Map<String, Object> param, String paramName, Class<T> t) {
		if (param != null) {
			Object result = param.get(paramName);
			if (result != null) {
				if (t == Long.class) {
					return (T) new Long(result.toString());
				}
				if (t == Integer.class) {
					return (T) new Integer(result.toString());
				}
				if (t == String.class) {
					return (T) result.toString();
				}
			}
		}
		return null;

	}

}
