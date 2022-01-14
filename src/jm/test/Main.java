package jm.test;

import java.util.List;

import jm.dao.DAO;
import jm.model.Utilisateur;

public class Main {
	public static void main(String[] args) throws Exception {
		try {
			List<Object> l = DAO.findAll(new Utilisateur());
			for (Object o : l) {
				Utilisateur t = (Utilisateur) o;
				System.out.println(t.getNom());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
 