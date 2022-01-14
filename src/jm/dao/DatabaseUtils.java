package jm.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jm.entity.reflection.EntityReflection;

public class DatabaseUtils {
	
	public static PreparedStatement generatePreparedStatementSelect(Connection co, Object ob, String id) throws SQLException {
		String selectQuery = generateSelectQuery(ob, id);
		return co.prepareStatement(selectQuery);
	}
	
	public static PreparedStatement generatePreparedStatementInsert(Connection co, Object ob) throws NoSuchMethodException, SecurityException, NoSuchFieldException, SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Method> gettersMethods = EntityReflection.getColumnsSettersOrGettersMethods(ob, "get");
		int size = gettersMethods.size();
		String insertQuery = generateInsertQuery(ob, size);
		PreparedStatement pst = co.prepareStatement(insertQuery);
		for(int i=0; i<size; i++) {
			Method getter = gettersMethods.get(i);
			Object value = getter.invoke(ob, (Object[])null);
			setObject(pst, i+1, value);
		}
		return pst;
	}
	
	public static PreparedStatement generatePreparedStatementUpdate(Connection co, Object ob) throws NoSuchMethodException, SecurityException, NoSuchFieldException, SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Method> gettersMethods = EntityReflection.getColumnsSettersOrGettersMethods(ob, "get");
		int size = gettersMethods.size();
		String updateQuery = generateUpdateQuery(ob);
		PreparedStatement pst = co.prepareStatement(updateQuery);
		int i;
		for(i=0; i<size; i++) {
			Method getter = gettersMethods.get(i);
			Object value = getter.invoke(ob, (Object[])null);
			setObject(pst, i+1, value);
		}
		Method getter = ob.getClass().getDeclaredMethod("getId", (Class<?>[])null);
		Object value = getter.invoke(ob, (Object[])null);
		setObject(pst, i+1, value);
		return pst;
	}
	
	public static PreparedStatement generatePreparedStatementDelete(Connection co, Object ob) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String deleteQuery = generateDeleteQuery(ob);
		PreparedStatement pst = co.prepareStatement(deleteQuery);
		Method getter = ob.getClass().getDeclaredMethod("getId", (Class<?>[])null);
		Object value = getter.invoke(ob, (Object[])null);
		setObject(pst, 1, value);
		return pst;
	}
	
	public static List<Object> getListObjectDatabase(Object ob, ResultSet rs) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException, SQLException {
		List<Object> listObject = new ArrayList<Object>();
		while(rs.next()) {
			@SuppressWarnings("deprecation")
			Object tempObject = ob.getClass().newInstance();
			List<Method> columnsSetterMethodsList = EntityReflection.getColumnsSettersOrGettersMethods(tempObject, "set");
			for(int i=0; i<columnsSetterMethodsList.size(); i++) {
				Method setterMethod = columnsSetterMethodsList.get(i);
				Object columnValue = rs.getObject(i+1);
				setterMethod.invoke(tempObject, columnValue);
			}
			listObject.add(tempObject);
		}
		return listObject;
	}
	
	private static String getColumnsQuery(Object ob, String selectOrInsert) {
		if(selectOrInsert.compareTo("select")!=0 && selectOrInsert.compareTo("insert")!=0)
			throw new IllegalArgumentException("The second argument must be \"select\" or \"insert\"");
		List<String> columnsAnnotationsNames = EntityReflection.getColumnsAnnotationsNames(ob);
		boolean select = selectOrInsert.compareTo("select")==0;
		String columnsQuery = (select)? "": "(";
		int size = columnsAnnotationsNames.size();
		for(int i=0; i<size; i++) {
			columnsQuery = columnsQuery.concat(columnsAnnotationsNames.get(i).concat(", "));
		}
		columnsQuery = columnsQuery.substring(0, columnsQuery.length()-2);
		return (select)? columnsQuery: columnsQuery.concat(")");
	}
	
	private static String generateSelectQuery(Object ob, String id) {
		String query = "SELECT %s FROM %s";
		query = (id == null) ? query : query.concat(" WHERE ID = ".concat(id));
		return String.format(query, getColumnsQuery(ob, "select"), EntityReflection.getTableAnnotationName(ob));
	}
	
	private static String generateInsertQuery(Object ob, int valueSize) {
		String insertQuery = "INSERT INTO %s %s VALUES (";
		insertQuery = String.format(insertQuery, EntityReflection.getTableAnnotationName(ob), getColumnsQuery(ob, "insert"));
		for(int i=0; i<valueSize; i++) {
			insertQuery = insertQuery.concat("?, ");
		}
		return insertQuery.substring(0, insertQuery.length()-2).concat(")");
	}
	
	private static String generateUpdateQuery(Object ob) {
		String updateQuery = String.format("UPDATE %s SET ", EntityReflection.getTableAnnotationName(ob));
		List<String> columnsNames = EntityReflection.getColumnsAnnotationsNames(ob);
		for(String columnName : columnsNames) {
			updateQuery = updateQuery.concat(String.format("%s=?, ", columnName));
		}
		updateQuery = updateQuery.substring(0, updateQuery.length()-2).concat(" WHERE ID=?");
		return updateQuery;
	}
	
	private static String generateDeleteQuery(Object ob) {
		return String.format("DELETE FROM %s WHERE ID=?", EntityReflection.getTableAnnotationName(ob));
	}
	
	private static void setObject(PreparedStatement pst, int parameterIndex, Object value) throws SQLException {
		if(value.getClass() == Date.class) {
			pst.setObject(parameterIndex, value, Types.TIMESTAMP);
		} else {
			pst.setObject(parameterIndex, value);
		}
	}
	
	public static void close(PreparedStatement pst, ResultSet rs) throws Exception {
		try {
			if(pst != null) pst.close();
			if(rs != null) rs.close();
		} catch(Exception e) {
			throwException(e);
		}
	}
	
	public static void throwException(Exception e) throws Exception {
		e.printStackTrace();
		throw e;
	}
}
