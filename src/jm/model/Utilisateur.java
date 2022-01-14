package jm.model;

import java.util.Date;

import jm.custom.annotation.Colonne;
import jm.custom.annotation.Entite;
import jm.custom.annotation.Tableau;

@Entite
@Tableau(nom = "utilisateur")
public class Utilisateur extends BaseModel {
	
	@Colonne(nom = "nom")
	private String nom;
	
	@Colonne(nom = "profil")
	private int profil;
	
	@Colonne(nom = "naissance")
	private Date naissance;
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public int getProfil() {
		return profil;
	}
	
	public void setProfil(int profil) {
		this.profil = profil;
	}
	
	public Date getNaissance() {
		return this.naissance;
	}
	
	public void setNaissance(Date naissance) {
		this.naissance = naissance;
	}
	
	public Utilisateur(String nom, int profil, Date naissance) {
		super();
		this.setNom(nom);
		this.setProfil(profil);
		this.setNaissance(naissance);
	}
	
	public Utilisateur() {
		super();
	}
			
}
