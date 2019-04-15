package controllers;

import java.util.Date;
import java.util.List;

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

	@Check({"Civil"})
	public static void index() {
        List<Super> superheros = Super.getSuperType(true);
	    render(superheros);
	}
	
	public static void create() {
        List<Civil> civils = Civil.findAll();
        render(civils);
    }
	
	public static void update(long id) {
		List<Civil> civils = Civil.findAll();
        Super superhero = Super.findById(id);
        render(superhero, civils);
	}
	
	public static void postCreate(@Valid Super superhero, long civil) {
		superhero.civil = Civil.findById(civil);
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
		superhero.civil = Civil.findById(Long.parseLong(params.data.get("civil")[0]));
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
