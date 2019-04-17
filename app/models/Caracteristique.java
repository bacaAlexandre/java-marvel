package models;


import play.db.jpa.Model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Caracteristique extends NewModel {


    @Column(length=100, nullable = false)
    public String libelle;

    @Column(columnDefinition = "text")
    public String description;

    public Boolean isAvantage = false;
    
    public static List<Caracteristique> getCaracteristiqueType(boolean isAvantage) {
    	List<Caracteristique> caracteristiques = find("byIsAvantage", isAvantage).fetch();
    	return caracteristiques;
    }
	
	public String getNameForDropdown() {
		return this.libelle.toString();
	}
}
