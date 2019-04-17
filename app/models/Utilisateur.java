package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.mindrot.jbcrypt.BCrypt;

import play.Logger;
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

    @ManyToOne
    public Role role;

	@ManyToOne
	public Civil civil;

	public boolean isAdmin = false;

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	public boolean can(String controller, String permission) {
		return this.can(controller, permission, null);
    }
	
	public boolean can(String controller, String permission, Long civil_id) {
		if (this.isAdmin) return true;
		RolePermission role_permission = this.getPermission(controller, permission);
		if (role_permission != null) {
			return civil_id != null ? this.civil.id == civil_id || role_permission.hasAll : true;
		}
		return false;
	}
	
	public RolePermission getPermission(String controller, String permission) {
		Set<RolePermission> permissions = this.role.rolePermission;
		return permissions.stream().filter(p -> {
			return (p.controller.equals(controller) && (p.permission.libelle.equals(permission)) || p.permission.libelle.equals("*"));
		}).findAny().orElse(null);
	}

    public static Utilisateur connect(String email, String password) {
    	Utilisateur utilisateur = find("byEmail", email).first();
    	if ((utilisateur != null) && (BCrypt.checkpw(password, utilisateur.password))) {
    		return utilisateur;
    	}
    	return null;
    }

    public SurEtre getHero() {
    	return SurEtre.find("byCivil", civil).first();
    }
    
    public boolean isHero() {
    	return getHero() != null;
    }

}
