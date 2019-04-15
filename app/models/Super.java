package models;


import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Super extends NewModel {

	@Required
    @Column(length = 100, nullable = false)
    public String nom;

    @Column(columnDefinition = "text")
    public String commentaire;

    public Boolean isHero = false;

    @ManyToMany
    public List<Caracteristique> avantage;

    @ManyToMany
    public List<Caracteristique> desavantage;

	@ManyToOne
	public Civil civil;
	
    public static List<Super> getSuperType(boolean isHero) {
    	List<Super> superheros = find("byIsHero", isHero).fetch();
    	return superheros;
    }
}
