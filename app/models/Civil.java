package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import net.sf.oval.constraint.MaxSize;
import play.db.jpa.Model;

@Entity
public class Civil extends NewModel {

	@Column(length=100,nullable=false)
	public String nom;

	@Column(length=100,nullable=false)
	public String prenom;
	
	@Column(columnDefinition="text")
	public String adresse;

	@Column(length=100)
	public String tel;

	@Column(columnDefinition="datetime",nullable=false)
	public Date dateNaissance;
	
	@Column(columnDefinition="datetime")
	public Date dateDeces;
	
	@Column(columnDefinition="text")
	public String commentaire;
	
	@Column(columnDefinition="datetime",nullable=false)
	public Date dateAjout;
	
	@Column(columnDefinition="datetime")
	public Date dateModification;
	
	@ManyToOne
	public Pays paysResidence;

	@ManyToOne
	public Pays paysNatal;

	@ManyToOne
	public GenreSexuel civilite;
}