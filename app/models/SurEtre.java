package models;


import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lib.To_form;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class SurEtre extends NewModel {

	@To_form
    @Column(length = 100, nullable = false)
	@Required
    public String nom;

	@To_form
    @Column(columnDefinition = "text")
    public String commentaire;

    public Boolean isHero = false;

	@To_form
    @ManyToMany
    public List<Avantage> avantages;

	@To_form
    @ManyToMany
    public List<Desavantage> desavantages;

	@To_form
	@ManyToOne
	public Civil civil;
	
    public static List<SurEtre> getSurEtreType(boolean isHero) {
    	List<SurEtre> superheros = find("byIsHero", isHero).fetch();
    	return superheros;
    }
	
	public String getNameForDropdown() {
		return this.nom.toString();
	}
}
