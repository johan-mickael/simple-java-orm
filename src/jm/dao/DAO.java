package jm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jm.helper.SQLHelper;

public class DAO {
	
	public static List<Object> findAll(Object ob) throws ClassNotFoundException, Exception {
		return findAll(SQLHelper.getConnection(), ob);
	}
	
	public static List<Object> findAll(Connection co, Object ob) throws Exception {
		return find(co, ob, null);
	}
	
	public static List<Object> findById(Connection co, Object ob, String id) throws Exception {
		return find(co, ob, id);
	}
	
	public static boolean insert(Connection co, Object ob) throws Exception {
		boolean isInserted = false;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = DatabaseUtils.generatePreparedStatementInsert(co, ob);
			isInserted = pst.execute();
			co.commit();
		} catch (Exception e) {
			co.rollback();
			DatabaseUtils.throwException(e);
		} finally {
			DatabaseUtils.close(pst, rs);
		}
		return isInserted;
	}
	
	public static boolean update(Connection co, Object ob) throws Exception {
		boolean isUpdated = false;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = DatabaseUtils.generatePreparedStatementUpdate(co, ob);
			isUpdated = pst.execute();
			co.commit();
		} catch (Exception e) {
			co.rollback();
			DatabaseUtils.throwException(e);
		} finally {
			DatabaseUtils.close(pst, rs);
		}
		return isUpdated;
	}
	
	public static boolean delete(Connection co, Object ob) throws Exception {
		boolean isDeleted = false;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = DatabaseUtils.generatePreparedStatementDelete(co, ob);
			isDeleted = pst.execute();
			co.commit();
		} catch (Exception e) {
			co.rollback();
			DatabaseUtils.throwException(e);
		} finally {
			DatabaseUtils.close(pst, rs);
		}
		return isDeleted;
	}
	
	
	public static List<Object> find(Connection co, Object ob, String id) throws Exception {
		List<Object> listObject = new ArrayList<Object>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = DatabaseUtils.generatePreparedStatementSelect(co, ob, id);
			rs = pst.executeQuery();			
			listObject = DatabaseUtils.getListObjectDatabase(ob, rs);
		} catch(SQLException e) {
			DatabaseUtils.throwException(e);
		} finally {
			DatabaseUtils.close(pst, rs);
		}
		return listObject;
	}
	
}
