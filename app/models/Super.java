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

    @Column(length = 100, nullable = false)
	@Required
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
	
	public static List<Super> getSuperType(boolean isHero){
        List<Super> supers = Super.findAll();
        List<Super> heros = new ArrayList<Super>();
        List<Super> vilains = new ArrayList<Super>();
    	for(Super thisSuper : supers) {
    		if(thisSuper.isHero) {
    			heros.add(thisSuper);
    		}
    		else{
    			vilains.add(thisSuper);
    		}
		}

        if(isHero) {
        	return heros;
        }
        else {
        	return vilains;
        }
	}
}
