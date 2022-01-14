package jm.entity.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {
	
	private static String getFieldSetterMethodName(String getterOrSetter, String fieldName) {
		if(getterOrSetter.compareTo("set")!=0 && getterOrSetter.compareTo("get")!=0)
			throw new IllegalArgumentException("The second argument must be \"get\" or \"set\"");
		String firstCharacter = (fieldName.charAt(0) + "").toUpperCase();
		return getterOrSetter + firstCharacter + fieldName.substring(1, fieldName.length());
	}
	
	private static Class<?> getFieldType(Object ob, String fieldName) throws NoSuchFieldException, SecurityException {
		Field field = ob.getClass().getDeclaredField(fieldName);
		return field.getType();
	}
	
	public static Method getSetterOrGetterMethod(Object ob, String getterOrSetter, String fieldName) throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		boolean setter = getterOrSetter.compareTo("set")==0;
		String fieldSetterMethodName = getFieldSetterMethodName(getterOrSetter, fieldName);
		Class<?> fieldType = getFieldType(ob, fieldName);
		Method setterOrGetterMethod = null;
		setterOrGetterMethod =  setter ?	ob.getClass().getDeclaredMethod(fieldSetterMethodName, fieldType)
									   :	ob.getClass().getDeclaredMethod(fieldSetterMethodName, (Class<?>[]) null);
		return setterOrGetterMethod;
	}
	
}
