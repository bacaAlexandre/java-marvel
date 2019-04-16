package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import lib.To_form;
import net.sf.oval.constraint.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Civil extends NewModel {

	@To_form
	@Column(length=100,nullable=false)
	@Required
	public String nom;

	@To_form
	@Column(length=100,nullable=false)
	@Required
	public String prenom;
	
	@To_form
	@Column(columnDefinition="text")
	public String adresse;

	@To_form
	@Column(length=20)
	public String tel;

	@To_form
	@Column(columnDefinition="datetime",nullable=false)
	@Required
	public Date dateNaissance;
	
	@To_form
	@Column(columnDefinition="datetime")
	public Date dateDeces;
	
	@To_form
	@Column(columnDefinition="text")
	public String commentaire;
	
	@Column(columnDefinition="datetime",nullable=false)
	public Date dateAjout;
	
	@Column(columnDefinition="datetime")
	public Date dateModification;
	
	@To_form
	@ManyToOne
	public Pays paysResidence;

	@To_form
	@ManyToOne
	public Pays paysNatal;

	@To_form
	@ManyToOne
	public GenreSexuel civilite;
	
	public static List<Civil> getByIds(List<Long> ids) {
		List<Civil> liste = new ArrayList<Civil>();
		for(Long id : ids) {
			liste.add(Civil.findById(id));
		}
		return liste;
	}

	public static List<Civil> getByIds(String[] strings) {
		List<Civil> liste = new ArrayList<Civil>();
		for(String id : strings) {
			liste.add(Civil.findById(Long.parseLong(id)));
		}
		return liste;
	}
	
	public String getNameForDropdown() {
		return this.nom.toString() + " " + this.prenom.toString();
	}
}
