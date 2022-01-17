package jm.model;

import java.util.Date;

import jm.custom.annotation.Column;
import jm.custom.annotation.Entity;
import jm.custom.annotation.Table;

@Entity
@Table(nom = "utilisateur")
public class Utilisateur extends BaseModel {
	
	@Column(nom = "nom")
	private String nom;
	
	@Column(nom = "profil")
	private int profil;
	
	@Column(nom = "naissance")
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
