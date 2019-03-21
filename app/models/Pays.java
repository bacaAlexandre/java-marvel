package models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Pays extends NewModel {

	@Column(length=100,nullable=false)
	public String nom;
	
}
