package jm.entity.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import jm.custom.annotation.AnnotationProcess;
import jm.custom.annotation.Colonne;

public class EntityReflection {
	
	public static String getTableAnnotationName(Object ob) {
		return AnnotationProcess.tableau(ob).nom();
	}
	
	public static List<String> getColumnsAnnotationsNames(Object ob) {
		List<String> columnsAnnotationsNamesList = new ArrayList<String>();
		List<Colonne> colonnesAnnotationsList = AnnotationProcess.colonnes(ob);
		for(Colonne colonneItem : colonnesAnnotationsList) {
			columnsAnnotationsNamesList.add(colonneItem.nom());
		}
		return columnsAnnotationsNamesList;
	}
	
	public static List<String> getFieldsNamesWithAnnotations(Object ob) {
		List<String> fieldsNamesWithAnnotations = new ArrayList<String>();
		List<Field> fieldsWithAnnotations = AnnotationProcess.getFieldsWithAnnotations(ob);
		for(Field field : fieldsWithAnnotations) {
			fieldsNamesWithAnnotations.add(field.getName());
		}
		return fieldsNamesWithAnnotations;
	}
	
	public static List<Method> getColumnsSettersOrGettersMethods(Object ob, String getterOrSetter) throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		List<Method> methodsList = new ArrayList<Method>();
		List<String> fieldsNamesWithAnnotations = getFieldsNamesWithAnnotations(ob);
		for(String columnName : fieldsNamesWithAnnotations) {
			methodsList.add(ReflectUtils.getSetterOrGetterMethod(ob, getterOrSetter, columnName));
		}
		return methodsList;
	}
	
}
