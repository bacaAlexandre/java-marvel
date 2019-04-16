package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lib.Genform;
import models.Caracteristique;
import models.Civil;
import models.GenreSexuel;
import models.Pays;
import models.SurEtre;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(AuthController.class)
public class VilainController extends Controller {
	
	public static void index() {
        List<SurEtre> vilains = SurEtre.getSurEtreType(false);
	    render(vilains);
	}
	
	public static void create() {
        String form = new Genform(new SurEtre(), "/vilain/add", "crudform").generate(validation.errorsMap(), flash);
        render("VilainController/form.html", form);
    }
	
	public static void postCreate(@Valid SurEtre suretre) {
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            create();
        }
		suretre.civil = Civil.findById(params.get("suretre.civil", Long.class));
		Long[] avantages = params.get("suretre.avantages", Long[].class);
		Long[] desavantages = params.get("suretre.desavantages", Long[].class);
		if (avantages != null) {
			suretre.avantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(avantages)).fetch());
		}
		if (desavantages != null) {
			suretre.desavantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(desavantages)).fetch());
		}
		suretre.isHero = false;
		suretre.save();
		index();
    }

	public static void update(long id) {
        SurEtre vilain = SurEtre.findById(id);
        String form = new Genform(vilain, "/vilain/update/"+id, "crudform").generate(validation.errorsMap(), flash);
        render("VilainController/form.html", vilain, form);
	}
	
	public static void postUpdate(long id) {
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            update(id);
        }
		SurEtre vilain = SurEtre.findById(id);
		Long[] avantages = params.get("suretre.avantages", Long[].class);
		Long[] desavantages = params.get("suretre.desavantages", Long[].class);
		if (avantages != null) {
			vilain.avantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(avantages)).fetch());
		}
		if (desavantages != null) {
			vilain.desavantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(desavantages)).fetch());
		}
		vilain.civil = Civil.findById(params.get("suretre.civil", Long.class));
		vilain.save();
		index();
    }
	
	public static void delete(long id) {
		SurEtre vilain = SurEtre.findById(id);
		vilain._delete();
		index();
	}
}
