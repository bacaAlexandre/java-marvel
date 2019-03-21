package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

@Entity
public class Droit extends Model {

	public String nom;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();

}
