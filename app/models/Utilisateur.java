package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Utilisateur extends NewModel {

	@Column(nullable=false)
	public String login;

	@Column(nullable=false)
	public String password;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Droit> droits = new ArrayList<Droit>();
	
	@ManyToOne
	public Civil civil;

}
