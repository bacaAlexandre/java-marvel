package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import models.Caracteristique;
import models.Civil;
import models.GenreSexuel;
import models.Organisation;
import models.Pays;
import models.Super;
import models.Utilisateur;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(Registration.class)
public class SuperHeroController extends Controller {

	@Check({"civil"})
	public static void index() {
        List<Super> superheros = Super.getSuperType(true);
	    render(superheros);
	}
	
	public static void create() {
        List<Civil> civils = Civil.findAll();
        List<Caracteristique> avantages = Caracteristique.getCaracteristiqueType(true);
        List<Caracteristique> desavantages = Caracteristique.getCaracteristiqueType(false);
        render(civils, avantages, desavantages);
    }
	
	public static void update(long id) {
		List<Civil> civils = Civil.findAll();
        Super superhero = Super.findById(id);
        List<Caracteristique> avantages = Caracteristique.getCaracteristiqueType(true);
        List<Caracteristique> desavantages = Caracteristique.getCaracteristiqueType(false);
        render(superhero, civils, avantages, desavantages);
	}
	
	public static void postCreate(@Valid Super superhero, long civil) {
		superhero.civil = Civil.findById(civil);
		Long[] avantages = params.get("avantages", Long[].class);
		Long[] desavantages = params.get("desavantages", Long[].class);
		List<Caracteristique> caracteristiques = new ArrayList<Caracteristique>();
		if (avantages != null) {
			caracteristiques.addAll(Caracteristique.find("id in (?1)", Arrays.asList(avantages)).fetch());
		}
		if (desavantages != null) {
			caracteristiques.addAll(Caracteristique.find("id in (?1)", Arrays.asList(desavantages)).fetch());
		}
		superhero.caracteristique = caracteristiques;
		superhero.isHero = true;
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            create();
        }
		superhero.save();
		index();
    }
	
	public static void postUpdate(long id) {
		Super superhero = Super.findById(id);
		superhero.edit(params.getRootParamNode(), "superhero");
		Long[] avantages = params.get("avantages", Long[].class);
		Long[] desavantages = params.get("desavantages", Long[].class);
		List<Caracteristique> caracteristiques = new ArrayList<Caracteristique>();
		if (avantages != null) {
			caracteristiques.addAll(Caracteristique.find("id in (?1)", Arrays.asList(avantages)).fetch());
		}
		if (desavantages != null) {
			caracteristiques.addAll(Caracteristique.find("id in (?1)", Arrays.asList(desavantages)).fetch());
		}
		superhero.caracteristique = caracteristiques;
		superhero.civil = Civil.findById(params.get("civil", Long.class));
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            update(superhero.id);
        }
		superhero.save();
		index();
    }
	
	public static void postDelete(long id) {
		Super superhero = Super.findById(id);
		superhero.delete();
		index();
	}
}
