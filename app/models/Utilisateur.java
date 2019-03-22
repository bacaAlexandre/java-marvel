package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.mindrot.jbcrypt.BCrypt;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Utilisateur extends NewModel {

	@Column(nullable=false, unique=true)
	@Required
	public String email;

	@Column(nullable=false)
	@Required
	public String password;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Droit> droits = new ArrayList<Droit>();

	@ManyToOne
	public Civil civil;

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

    public static Utilisateur connect(String email, String password) {
    	Utilisateur utilisateur = find("byEmail", email).first();
    	if ((utilisateur != null) && (BCrypt.checkpw(password, utilisateur.password))) {
    		return utilisateur;
    	}
    	return null;
    }

}
