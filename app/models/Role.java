package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Role extends NewModel {
	
	@Column(nullable=false)
	public String libelle;
	
	@OneToMany(mappedBy = "role")
    public Set<RolePermission> rolePermission = new HashSet<RolePermission>();

}
