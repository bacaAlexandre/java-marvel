package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="role_permission")
public class RolePermission extends NewModel {
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "role_id")
    public Role role;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permission_id")
    public Permission permission;

    @Column(length=100,nullable=false)
    public String controller;
    
    public boolean hasAll = false;
}
