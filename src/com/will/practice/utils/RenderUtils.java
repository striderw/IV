package com.will.practice.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderUtils {

	private RenderUtils() {

	}

	public static <T>Map<String, String> renderListToMap(List<T> list, String[] properties) {
		Map<String, String> retMap = new HashMap<String, String>();
		if (null == list || list.isEmpty() || null == properties || properties.length <= 0) {
			return retMap;
		}
		T obj = list.get(0);
		for (String property : properties) {
			Object val = BeanUtils.getFieldValue(obj, property);
			if (null == val) {
				retMap.put(property, null);
			} else {
				retMap.put(property, val.toString());
			}
		}
		return retMap;
	}
}
