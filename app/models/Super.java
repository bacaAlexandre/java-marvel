package models;


import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Super extends NewModel {

    @Column(length = 100, nullable = false)
	@Required
    public String nom;

    @Column(columnDefinition = "text")
    public String commentaire;

    public Boolean isHero = false;

    @ManyToMany
    public List<Caracteristique> caracteristique;

	@ManyToOne
	public Civil civil;
	
    public static List<Super> getSuperType(boolean isHero) {
    	List<Super> superheros = find("byIsHero", isHero).fetch();
    	return superheros;
    }
    
    public List<Caracteristique> getCaracteristiqueType(boolean isAvantage) {
    	return this.caracteristique.stream().filter(p -> p.isAvantage == isAvantage).collect(Collectors.toList());
    }
}
