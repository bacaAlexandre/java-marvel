package models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Pays extends NewModel {

	@Column(nullable=false)
	public String nom;
	
}
