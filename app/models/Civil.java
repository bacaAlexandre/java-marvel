package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import net.sf.oval.constraint.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Civil extends NewModel {

	@Column(length=100,nullable=false)
	@Required
	public String nom;

	@Column(length=100,nullable=false)
	@Required
	public String prenom;
	
	@Column(columnDefinition="text")
	public String adresse;

	@Column(length=20)
	public String tel;

	@Column(columnDefinition="datetime",nullable=false)
	@Required
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
}
