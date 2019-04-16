package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lib.Check;
import lib.Genform;
import models.Caracteristique;
import models.Civil;
import models.GenreSexuel;
import models.Organisation;
import models.Pays;
import models.SurEtre;
import models.Utilisateur;
import play.Logger;
import play.data.validation.Error;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(AuthController.class)
public class SuperHeroController extends Controller {

	@Check({"civil"})
	public static void index() {
        List<SurEtre> superheros = SurEtre.getSurEtreType(true);
	    render(superheros);
	}
	
	public static void create() {
        String form = new Genform(new SurEtre(), "/superhero/add", "crudform").generate(validation.errorsMap(), flash);
        render("SuperHeroController/form.html", form);
    }
	
	public static void postCreate(@Valid SurEtre suretre) {
		Long civilID = params.get("suretre.civil", Long.class);
		if(civilID == -1) {
			validation.addError("suretre.civil", "Required", "");
		}
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            create();
        }
		suretre.civil = Civil.findById(civilID);
		Long[] avantages = params.get("suretre.avantages", Long[].class);
		Long[] desavantages = params.get("suretre.desavantages", Long[].class);
		if (avantages != null) {
			suretre.avantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(avantages)).fetch());
		}
		if (desavantages != null) {
			suretre.desavantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(desavantages)).fetch());
		}
		suretre.isHero = true;
		suretre.save();
		index();
    }
	
	public static void update(long id) {
        SurEtre superhero = SurEtre.findById(id);
        String form = new Genform(superhero, "/superhero/update/"+id, "crudform").generate(validation.errorsMap(), flash);
        render("SuperHeroController/form.html", superhero, form);
	}
	
	public static void postUpdate(long id) {
		Long civilID = params.get("suretre.civil", Long.class);
		if(civilID == -1) {
			validation.addError("suretre.civil", "Required", "");
		}
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            update(id);
        }
		SurEtre superhero = SurEtre.findById(id);
		Long[] avantages = params.get("suretre.avantages", Long[].class);
		Long[] desavantages = params.get("suretre.desavantages", Long[].class);
		if (avantages != null) {
			superhero.avantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(avantages)).fetch());
		}
		if (desavantages != null) {
			superhero.desavantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(desavantages)).fetch());
		}
		superhero.civil = Civil.findById(civilID);
		superhero.save();
		index();
    }
	
	public static void delete(long id) {
		SurEtre superhero = SurEtre.findById(id);
		superhero.delete();
		index();
	}
}
