package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

@Entity
public class Utilisateur extends NewModel {

	public String login;

	public String password;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Droit> droits = new ArrayList<Droit>();

}
