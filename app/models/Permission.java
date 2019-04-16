package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Permission extends NewModel {
	
	@Column(nullable=false)
	public String libelle;
	
	@OneToMany(mappedBy = "permission")
    public Set<RolePermission> rolePermission = new HashSet<RolePermission>();
}
