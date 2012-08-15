package org.hwork.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 泛型工具类
 */
public class GenericsUtils {
	/**
	 * 通过反射获取该类父类泛型参数
	 * <p>例如：BlogModel extends Model<Blog> 通过此方法获取到Blog类的Class<?></p>
	 * @param clazz
	 * @param index
	 * @return
	 */
	public static Class<?> getSuperClassGenericType(Class<?> clazz, int index) {
		//获取此类的直接超类的Type
		Type genType = clazz.getGenericSuperclass();
		if(!(genType instanceof ParameterizedType)){
			//如果不包含泛型化参数就直接返回Object的class
			return Object.class;
		}
		ParameterizedType paraType = (ParameterizedType)genType;
		Type[] types = paraType.getActualTypeArguments();
		if(index < 0 || index >= types.length){
			throw new RuntimeException("你要获取的类没有泛型参数，或者你所要的索引 " + index + "超过了最大参数 " + types.length);
		}
		if(!(types[index] instanceof Class<?>)){
			return Object.class;
		}
		return (Class<?>)types[index];
	}

	/**
	 * 获取第一个泛型化参数
	 * @param clazz
	 * @return
	 */
	public static Class<?> getSuperClassGenericType(Class<?> clazz) {
		return getSuperClassGenericType(clazz, 0);
	}

	/**
	 * 通过反射,获得方法返回值泛型参数的实际类型. 如: public Map<String, Buyer> getNames(){}
	 * @param method
	 * @param index
	 * @return
	 */
	public static Class<?> getMethodGenericReturnType(Method method, int index) {
		Type returnType = method.getGenericReturnType();
		if (!(returnType instanceof ParameterizedType)) {
			return Object.class;
		}
		ParameterizedType type = (ParameterizedType) returnType;
		Type[] typeArguments = type.getActualTypeArguments();
		if (index >= typeArguments.length || index < 0) {
			throw new RuntimeException("你要获取的类没有泛型参数，或者你所要的索引 " + index + "超过了最大参数 " + typeArguments.length);
		}
		return (Class<?>) typeArguments[index];
	}

	/**
	 * 通过反射,获得方法返回值第一个泛型参数的实际类型. 如: public Map<String, Buyer> getNames(){}
	 * @param method
	 * @return
	 */
	public static Class<?> getMethodGenericReturnType(Method method) {
		return getMethodGenericReturnType(method, 0);
	}

	/**
	 * 通过反射,获得方法输入参数第index个输入参数的所有泛型参数的实际类型. 如: public void add(Map<String,Buyer> maps, List<String> names){}
	 * @param method
	 * @param index
	 * @return
	 */
	public static List<Class<?>> getMethodGenericParameterTypes(Method method,
			int index) {
		List<Class<?>> results = new ArrayList<Class<?>>();
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		if (index >= genericParameterTypes.length || index < 0) {
			throw new RuntimeException("你要获取的类没有泛型参数，或者你所要的索引 " + index + "超过了最大参数 " + genericParameterTypes.length);
		}
		Type genericParameterType = genericParameterTypes[index];
		if (genericParameterType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericParameterType;
			Type[] parameterArgTypes = aType.getActualTypeArguments();
			for (Type parameterArgType : parameterArgTypes) {
				Class<?> parameterArgClass = (Class<?>) parameterArgType;
				results.add(parameterArgClass);
			}
			return results;
		}
		return results;
	}

	/**
	 * 通过反射,获得方法输入参数第一个输入参数的所有泛型参数的实际类型. 如: public void add(Map<String, Buyer>, List<String> names){}
	 * @param method
	 * @return
	 */
	public static List<Class<?>> getMethodGenericParameterTypes(Method method) {
		return getMethodGenericParameterTypes(method, 0);
	}

	/**
	 * 通过反射,获得Field泛型参数的实际类型. 如: public Map<String, Buyer> names;
	 * @param field
	 * @param index
	 * @return
	 */
	public static Class<?> getFieldGenericType(Field field, int index) {
		Type genericFieldType = field.getGenericType();
		if (genericFieldType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericFieldType;
			Type[] fieldArgTypes = aType.getActualTypeArguments();
			if (index >= fieldArgTypes.length || index < 0) {
				throw new RuntimeException("你要获取的类没有泛型参数，或者你所要的索引 " + index + "超过了最大参数 " + fieldArgTypes.length);
			}
			return (Class<?>) fieldArgTypes[index];
		}
		return Object.class;
	}

	/**
	 * 通过反射,获得Field泛型参数的实际类型. 如: public Map<String, Buyer> names;
	 * @param field
	 * @return
	 */
	public static Class<?> getFieldGenericType(Field field) {
		return getFieldGenericType(field, 0);
	}
}
