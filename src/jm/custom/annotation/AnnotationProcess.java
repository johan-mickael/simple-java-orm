package jm.custom.annotation;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnnotationProcess {
	
	private static void verifyAnnotationEntite(Class<?> objectClass) {
		if(!objectClass.isAnnotationPresent(Entity.class))
			throw new AnnotationTypeMismatchException(null, "La classe "+objectClass.getSimpleName()+" n'est pas annotée avec @Entite");
	}
	
	private static void verifyAnnotationTableau(Class<?> objectClass) {
		verifyAnnotationEntite(objectClass);
		if(!objectClass.isAnnotationPresent(Table.class))
			throw new AnnotationTypeMismatchException(null, "La classe "+objectClass.getSimpleName()+" n'est pas annotée avec @Tableau");
	}
	
	public static Table tableau(Object ob) throws IllegalArgumentException {
		if (Objects.isNull(ob)) throw new IllegalArgumentException();
		Class<?> objectClass = ob.getClass();
		verifyAnnotationTableau(objectClass);
		return objectClass.getAnnotation(Table.class);
	}
	
	public static List<Column> colonnes(Object ob) {
		List<Field> fieldsWithAnnotationsList = getFieldsWithAnnotations(ob);
		List<Column> colonneAnnotationList = new ArrayList<Column>();
		for(Field field : fieldsWithAnnotationsList) {
			colonneAnnotationList.add(field.getAnnotation(Column.class));
		}
		return colonneAnnotationList;
	}
	
	public static List<Field> getFieldsWithAnnotations(Object ob) {
		if (Objects.isNull(ob)) throw new IllegalArgumentException();
		Class<?> objectClass = ob.getClass();
		Field[] declaredFields = objectClass.getDeclaredFields();
		
		List<Field> fieldsWithAnnotationsList = new ArrayList<Field>();
		for(Field field : declaredFields) {
			if(field.isAnnotationPresent(Column.class)) fieldsWithAnnotationsList.add(field);
		}
		return fieldsWithAnnotationsList;
	}
}
