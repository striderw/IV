/*******************************************************************************
 * BeanUtils.java 01-00
 *
 * Copyright 2014 by LD, Ltd. All right reserved.
 *
 *	2014/08/07 01-00 wangmb
 *******************************************************************************/
package com.will.practice.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Bean工具方法类
 * @version  01-00
 */
public class BeanUtils {

	private BeanUtils() {
	}

	/**
	 * 比较两个对象，返回属性值不同的字段名
	 * 	（如果对象中没有响应的属性名，比较父对象的属性）
	 * @param 	origin  源对象
	 * @param 	target  目标对象
	 * @return	List<String>  属性列表
	 * @since 01-00
	 */
	public static List<String> objectDiff(Object origin, Object target) {
		return objectDiff(origin, target, true);
	}


	/**
	 * 比较两个对象，返回属性值不同的字段名
	 * @param 	origin  源对象
	 * @param 	target  目标对象
	 * @param 	containSuper 是否比较从父类继承的属性
	 * @return	List<String>  属性列表
	 * @since 01-00
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> objectDiff(Object origin, Object target,
			boolean containSuper) {
		Class originClazz = origin.getClass();
		Class targetClazz = target.getClass();
		Field[] fields = originClazz.getDeclaredFields();
		List<String> retList = new ArrayList<String>();
		for (Field f : fields) {

			String fName = f.getName();
			try {
				Class originFieldType = f.getType();
				Class targetFieldType = getDeclaredField(targetClazz, fName)
						.getType();

				// 如果属性的类型不同，则认为这两个属性不同，将属性名添加到属性列表
				if (originFieldType != targetFieldType) {
					retList.add(fName);
					continue;
				}

				Method originGetter = getGetterMethod(originClazz, fName,
						containSuper);
				Method targetGetter = getGetterMethod(targetClazz, fName,
						containSuper);

				// 如果属性的值都为null，则认为相同，继续下一次迭代
				if (null == targetGetter || null == originGetter) {
					continue;
				}

				Object originValue = originGetter.invoke(origin,
						new Object[] {});

				Object targetValue = originGetter.invoke(target,
						new Object[] {});

				// 如果源对象的属性值为null，目标对象的属性值也为null，则执行下一次迭代
				// 目标对象的属性值也不为null，则认为属性值不同，将属性名添加到属性列表
				if (null == originValue) {

					if (null == targetValue) {
						continue;
					} else {
						retList.add(fName);
					}

				// 如果源对象的属性值不为null，则比较两个对象的属性值
				// 如果属性值不相等，将属性名添加到属性列表，否则，执行下一次迭代
				} else {
					if (!originValue.equals(targetValue)) {
						retList.add(fName);
					} else {
						continue;
					}
				}

			// 异常发生时
			} catch (IllegalAccessException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			} catch (InvocationTargetException e) {
				continue;
			} catch (NoSuchFieldException e) {
				continue;
			}

		}
		return retList;
	}

	/**
	 * 判断两个对象的属性值是否相同
	 * @param 	origin  源对象
	 * @param 	target  目标对象
	 * @return	boolean 相同返回true，否则返回false
	 * @since 01-00
	 */
	public static boolean objectCompare(Object origin, Object target) {
		return objectCompare(origin, target, true);
	}

	/**
	 * 判断两个对象的属性值是否相同
	 * @param 	origin  源对象
	 * @param 	target  目标对象
	 * @param 	containSuper  是否比较从父类继承的属性
	 * @return	boolean 相同返回true，否则返回false
	 * @since 01-00
	 */
	@SuppressWarnings("rawtypes")
	public static boolean objectCompare(Object origin, Object target,
			boolean containSuper) {
		Class originClazz = origin.getClass();
		Class targetClazz = target.getClass();
		Field[] fields = originClazz.getDeclaredFields();
		for (Field f : fields) {

			String fName = f.getName();

			try {
				Class originFieldType = f.getType();
				Class targetFieldType = getDeclaredField(targetClazz, fName)
						.getType();
				// 如果两个对象的属性类型不同，返回false
				if (originFieldType != targetFieldType) {
					return false;
				}

				Method originGetter = getGetterMethod(originClazz, fName,
						containSuper);
				Method targetGetter = getGetterMethod(targetClazz, fName,
						containSuper);

				// 如果两个对象的属性值都为null，执行下次迭代
				if (null == targetGetter || null == originGetter) {
					continue;
				}

				Object originValue = originGetter.invoke(origin,
						new Object[] {});

				Object targetValue = originGetter.invoke(target,
						new Object[] {});

				// 如果源对象的属性值为null，目标对象的属性值也为null，则执行下一次迭代
				// 目标对象的属性值也不为null，则认为属性值不同，返回false
				if (null == originValue) {
					if (null == targetValue) {
						continue;
					} else {
						return false;
					}

				// 如果源对象的属性值不为null，则比较两个对象的属性值
				// 如果属性值不相等，false，否则执行下一次迭代
				} else {
					if (!originValue.equals(targetValue)) {
						return false;
					} else {
						continue;
					}
				}

			// 异常发生时
			} catch (IllegalAccessException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			} catch (InvocationTargetException e) {
				continue;
			} catch (NoSuchFieldException e) {
				continue;
			}
		}
		return true;
	}

	/**
	 * 对象复制
	 * 	注意：调用这个方法，1.如果目标对象自身没有某个属性，会去它的基类中查找
	 * 		2.如果字符串是空字符串，会被置为null
	 * 		3.自动去除前后空格
	 * @param 	origin  源对象
	 * @param 	target  目标对象
	 * @since 01-00
	 */
	public static void objectCopy(Object origin, Object target) {
		objectCopy(origin, target, true, true, true);
	}

	/**
	 * 对象复制
	 * 	注意：调用这个方法，1.如果目标对象自身没有某个属性，会去它的基类中查找
	 * 		2.如果字符串是空字符串，会被置为null
	 * 		3.自动去除前后空格
	 * @param 	origin  源对象
	 * @param 	target  目标对象
	 * @param 	ignoreProperties   要忽略的属性名字
	 * @since 01-00
	 */
	public static void objectCopy(Object origin, Object target, String... ignoreProperties) {
		objectCopy(origin, target, true, true, true, ignoreProperties);
	}

	/**
	 * 对象复制
	 * @param 	origin				源对象
	 * @param 	target				目标对象
	 * @param 	containSuper		是否比较从父类继承的属性
	 * @param 	nullIfBlank			是否将空字符串转换成null
	 * @param 	autoTrim			是否自动去除前后空
	 * @param 	ignoreProperties	要忽略的属性名字
	 * @since 01-00
	 */
	@SuppressWarnings("rawtypes")
	public static void objectCopy(Object origin, Object target, boolean containSuper, boolean nullIfBlank,
			boolean autoTrim, String... ignoreProperties) {
		Class originClazz = origin.getClass();
		Class targetClazz = target.getClass();
		Field[] fields = originClazz.getDeclaredFields();
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
		for (Field f : fields) {

			String fName = f.getName();
			// 如果这个属性在忽略列表中，则不处理
			if (ignoreProperties != null && ignoreList.contains(fName)) {
				continue;
			}

			try {
				Class originFieldType = f.getType();
				Class targetFieldType = getDeclaredField(targetClazz, fName)
						.getType();

				Method originGetter = getGetterMethod(originClazz, fName,
						containSuper);
				Method targetSetter = getSetterMethod(targetClazz, fName,
						targetFieldType, containSuper);

				// 如果targetSetter或originGetter为null，执行下一次迭代
				if (null == targetSetter || null == originGetter) {
					continue;
				}

				Object originValue = originGetter.invoke(origin,
						new Object[] {});

				// 源对象的属性是Boolean类型的情况
				if ("java.lang.Boolean".equals(originFieldType.getName())) {
					// 源对象的属性值是null的情况，直接复制null给目标对象
					if (null == originValue) {
						targetSetter.invoke(target, new Object[]{null});
						continue;
					}
					// 目标对象的属性是Integer类型的情况
					if ("java.lang.Integer".equals(targetFieldType.getName())) {
						targetSetter.invoke(target,
								boolToInt((Boolean) originValue));
						continue;
					}
					// 目标对象的属性是Long类型的情况
					if ("java.lang.Long".equals(targetFieldType.getName())) {
						targetSetter.invoke(target,
								boolToLong((Boolean) originValue));
						continue;
					}
					// 目标对象的属性是Boolean类型的情况
					if ("java.lang.Boolean".equals(targetFieldType.getName())) {
						targetSetter.invoke(target, originValue);
						continue;
					}

				// 源对象是String的情况
				} else if ("java.lang.String".equals(originFieldType.getName())) {
					String originValueStr = (String)originValue;
					// 如果originValueStr是空字符串，并且nullIfBlank为true
					if (StringUtils.isBlank(originValueStr) && nullIfBlank) {
						originValueStr = null;
					}
					if (null != originValueStr && autoTrim) {
						originValueStr = originValueStr.trim();
					}
					targetSetter.invoke(target, originValueStr);

				// 源对象不是Boolean和String的情况，直接复制属性值给目标对象
				} else {
					targetSetter.invoke(target, originValue);
				}

			// 异常发生时
			} catch (IllegalAccessException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			} catch (InvocationTargetException e) {
				continue;
			} catch (NoSuchFieldException e) {
				continue;
			}

		}
	}

	/**
	 * 转换boolean值为整型值
	 * @param 	origin  boolean
	 * @return	int		整型值
	 * @since 01-00
	 */
	public static int boolToInt(boolean origin) {
		// origin为true转换为1，否则转换为0
		if (origin) {
			return 1;
		}
		return 0;
	}

	/**
	 * 转换boolean值为长整型值
	 * @param 	origin  boolean
	 * @return	long	长整型值
	 * @since 01-00
	 */
	public static long boolToLong(boolean origin) {
		// origin为true转换为1L，否则转换为0L
		if (origin) {
			return 1L;
		}
		return 0L;
	}

	/**
	 * 取得对象object的fieldName属性的Field对象
	 * @param 	object 		对象
	 * @param 	fieldName 	属性名
	 * @return 	Field	Field对象
	 * @throws NoSuchFieldException
	 * @since 01-00
	 */
	public static Field getDeclaredField(Object object, String fieldName)
			throws NoSuchFieldException {
		return getDeclaredField(object.getClass(), fieldName);
	}

	/**
	 * 取得对象object的fieldName属性的Field对象
	 * @param 	object 		对象
	 * @param 	fieldName 	属性名
	 * @param 	containSuper  是否包含从父类继承的属性
	 * @return 	Field	Field对象
	 * @throws NoSuchFieldException
	 * @since 01-00
	 */
	public static Field getDeclaredField(Object object, String fieldName,
			boolean containSuper) throws NoSuchFieldException {
		return getDeclaredField(object.getClass(), fieldName, containSuper);
	}

	/**
	 * 取得Class对象的fieldName属性的Field对象
	 * @param 	clazz 		Class对象
	 * @param 	fieldName 	属性名
	 * @param 	containSuper  是否包含从父类继承的属性
	 * @return 	Field	Field对象
	 * @throws NoSuchFieldException
	 * @since 01-00
	 */
	@SuppressWarnings("rawtypes")
	public static Field getDeclaredField(Class clazz, String fieldName,
			boolean containSuper) throws NoSuchFieldException {
		for (Class superClass = clazz; superClass != Object.class;) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				if (containSuper) {
					superClass = superClass.getSuperclass();
				} else {
					break;
				}
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName()
				+ '.' + fieldName);
	}

	/**
	 * 取得Class对象的fieldName属性的Field对象
	 * @param 	clazz 		Class对象
	 * @param 	fieldName 	属性名
	 * @return 	Field	Field对象
	 * @throws NoSuchFieldException
	 * @since 01-00
	 */
	@SuppressWarnings("rawtypes")
	public static Field getDeclaredField(Class clazz, String fieldName)
			throws NoSuchFieldException {
		return getDeclaredField(clazz, fieldName, true);
	}

	/**
	 * 取得属性的get方法名
	 * @param 	fieldName  属性名
	 *
	 * @return String
	 * @since 01-00
	 */
	public static String getGetterName(String fieldName) {
		return "get" + StringUtils.capitalize(fieldName);
	}

	/**
	 * 取得Class对象的fieldName属性的get Method对象
	 * @param 	clazz 	Class对象
	 * @param 	fieldName	属性名
	 * @param 	containSuper  是否包含父类的属性
	 *
	 * @return Method	 Method对象
	 * @since 01-00
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Method getGetterMethod(Class clazz, String fieldName,
			boolean containSuper) {
		try {

			Method method = null;
			while (null == method) {
				method = clazz.getMethod(getGetterName(fieldName));
				if (containSuper) {
					clazz = clazz.getSuperclass();
				}
			}

			return method;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * 取得Class对象的fieldName属性的get Method对象（包含父类的属性）
	 * @param 	clazz 		Class对象
	 * @param 	fieldName	属性名
	 *
	 * @return Method	 Method对象
	 * @since 01-00
	 */
	@SuppressWarnings("rawtypes")
	public static Method getGetterMethod(Class clazz, String fieldName) {
		return getGetterMethod(clazz, fieldName, true);
	}

	/**
	 * 取得属性的set方法名
	 * @param 	fieldName  属性名
	 *
	 * @return String
	 * @since 01-00
	 */
	public static String getSetterName(String fieldName) {
		return "set" + StringUtils.capitalize(fieldName);
	}


	/**
	 * 取得Class对象的fieldName属性的set Method对象
	 * @param 	clazz		Class对象
	 * @param 	fieldName	属性名
	 * @param 	fieldType	属性的Class对象
	 * @param 	containSuper  是否包含父类的属性
	 *
	 * @return Method	 Method对象
	 * @since 01-00
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Method getSetterMethod(Class clazz, String fieldName,
			Class fieldType, boolean containSuper) {
		try {

			Method method = null;
			while (null == method) {
				method = clazz.getMethod(getSetterName(fieldName),
						new Class[] {fieldType});
				if (containSuper) {
					clazz = clazz.getSuperclass();
				}
			}

			return method;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * 取得Class对象的fieldName属性的set Method对象（包含父类的属性）
	 * @param 	clazz 	Class对象
	 * @param 	fieldName	属性名
	 * @param 	fieldType	属性的Class对象
	 *
	 * @return Method	 Method对象
	 * @since 01-00
	 */
	@SuppressWarnings("rawtypes")
	public static Method getSetterMethod(Class clazz, String fieldName,
			Class fieldType) {
		return getSetterMethod(clazz, fieldName, fieldType, true);
	}

	/**
	 * 取得对象的属性值
	 *
	 * @param 	obj			对象
	 * @param 	fieldName	属性名
	 *
	 * @return Object	属性值
	 * @since 01-00
	 */
	@SuppressWarnings("rawtypes")
	public static Object getFieldValue(Object obj, String fieldName) {
		Object val = null;
		try {
			if (null != obj && StringUtils.isNotBlank(fieldName)) {

				Class clazz = obj.getClass();
				if (!fieldName.contains(".")) {
					Method getter = getGetterMethod(clazz, fieldName);
					if (null != getter) {
						val = getter.invoke(obj, new Object[] {});
					}
				} else {
					String fieldNamePre = StringUtils.substringBefore(fieldName, ".");
					String fieldNameSuff = StringUtils.substringAfter(fieldName, ".");
					if (StringUtils.isBlank(fieldNamePre)) {
						return getFieldValue(obj, fieldNameSuff);
					} else {
						Method getter = getGetterMethod(clazz, fieldNamePre);
						if (null != getter) {
							Object objInner = getter.invoke(obj, new Object[] {});
							return getFieldValue(objInner, fieldNameSuff);
						}
					}

				}

			}
		} catch (Exception e) {
		}
		return val;
	}

	/**
	 * 设置对象的属性值
	 *
	 * @param 	obj			对象
	 * @param 	fieldName	属性名
	 *
	 * @return boolean	是否成功
	 * @since 01-00
	 */
	@SuppressWarnings("rawtypes")
	public static boolean setFieldValue(Object obj, String fieldName,
			Class valClazz, Object... val) {
		try {
			if (null != obj) {
				Class clazz = obj.getClass();
				Method setter = getSetterMethod(clazz, fieldName, valClazz);
				if (null != setter) {
					setter.invoke(obj, val);
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 取得classClazz的某个属性的annotationClazz对应的Annotation
	 * @param classClazz						类的Class对象
	 * @param fieldName							字段名字
	 * @param annotationClazz					Annotation的Class对象
	 * @return Annotation
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Annotation> T getFieldAnnotation(Class classClazz, String fieldName,
			Class<T> annotationClazz) {
		if (null == classClazz || null == fieldName || null == annotationClazz) {
			return null;
		}

		Field field;
		try {
			field = getDeclaredField(classClazz, fieldName);
		} catch (NoSuchFieldException e) {
			return null;
		}

		if (null == field) {
			return null;
		}
		return field.getAnnotation(annotationClazz);
	}

	/**
	 * 取得对象的某个属性的annotationClazz对应的Annotation
	 * @param obj								对象
	 * @param fieldName							字段名字
	 * @param annotationClazz					Annotation的Class对象
	 * @return Annotation
	 */
	public static <T extends Annotation> T getFieldAnnotation(Object obj, String fieldName,
			Class<T> annotationClazz) {
		if (null == obj) {
			return null;
		}
		return getFieldAnnotation(obj.getClass(), fieldName, annotationClazz);
	}

	/**
	 * 取得classClazz的某个属性的get方法的annotationClazz对应的Annotation
	 * @param classClazz						类的Class对象
	 * @param fieldName							字段名字
	 * @param annotationClazz					Annotation的Class对象
	 * @return Annotation
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Annotation> T getGetMethodAnnotation(Class classClazz, String fieldName,
			Class<T> annotationClazz) {
		if (null == classClazz || null == fieldName || null == annotationClazz) {
			return null;
		}
		Method getMethod = getGetterMethod(classClazz, fieldName, true);
		if (null == getMethod) {
			return null;
		}
		return getMethod.getAnnotation(annotationClazz);
	}

	/**
	 * 取得对象的某个属性的get方法的annotationClazz对应的Annotation
	 * @param obj								对象
	 * @param fieldName							字段名字
	 * @param annotationClazz					Annotation的Class对象
	 * @return Annotation
	 */
	public static <T extends Annotation> T getGetMethodAnnotation(Object obj, String fieldName,
			Class<T> annotationClazz) {
		if (null == obj) {
			return null;
		}
		return getGetMethodAnnotation(obj.getClass(), fieldName, annotationClazz);
	}
}
