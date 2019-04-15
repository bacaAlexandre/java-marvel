package models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Pays extends NewModel {

	@Column(nullable=false)
	public String libelle;
	
	public String getNameForDropdown() {
		return this.libelle.toString();
	}
	
}
