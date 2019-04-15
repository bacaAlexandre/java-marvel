package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
public class Organisation extends NewModel {

	@Required
	@Column(length=100,nullable=false)
	public String nom;

	@Required
	@Column(columnDefinition="text",nullable=false)
	public String adresse;
	
	@Column(columnDefinition="text")
	public String commentaire;	

	@Column(columnDefinition="datetime",nullable=false)
	public Date dateAjout;
	
	@Column(columnDefinition="datetime")
	public Date dateModification;
	
	@ManyToOne
	public Pays pays;
	
	@ManyToOne
	public Civil dirigeant;
	
	@ManyToMany
	public List<Civil> membres;
	
	public long getIncidentNumber() {
		return Incident.count("byOrganisation", this );
	}
	
	public long getMissionNumber() {
		return Incident.count("byOrganisationAndMissionIsNotNull", this);
	}
}
