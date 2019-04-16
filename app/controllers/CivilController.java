package controllers;

import java.util.Date;
import java.util.List;

import lib.Genform;
import lib.Check;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;
import models.Civil;
import models.Pays;
import models.Utilisateur;
import models.GenreSexuel;
import models.Organisation;

@With(AuthController.class)
@Check({"civil"})
public class CivilController extends Controller {
	
	public static void index() {
        List<Civil> civils = Civil.findAll();
	    render(civils);
	}
	
	public static void create() {
        List<Pays> pays = Pays.findAll();
        List<GenreSexuel> civilites = GenreSexuel.findAll();
        String form = new Genform(new Civil(), "/civil/add", "crudform").generate();
        render("CivilController/form.html", pays, civilites, form);
    }
	
	public static void postCreate(@Valid Civil civil) {
		Logger.info(""+(civil.paysResidence));
		civil.paysResidence = params.get("civil.paysResidence", Long.class) != -1 ? Pays.findById(params.get("civil.paysResidence", Long.class)) : null;
		Logger.info(""+(civil.paysResidence));
		civil.paysNatal = params.get("civil.paysNatal", Long.class) != -1 ? Pays.findById(params.get("civil.paysNatal", Long.class)) : null;
		civil.civilite = params.get("civil.civilite", Long.class) != -1 ? GenreSexuel.findById(params.get("civil.civilite", Long.class)) : null;
		if(validation.hasErrors()) {
			for(play.data.validation.Error error : validation.errors()) {
				Logger.info(error.getKey());
				Logger.info(error.message());
				Logger.info(error.getMessageKey());
				Logger.info("-----------");
			}
            params.flash();
            validation.keep();
            create();
        }
		civil.dateAjout = new Date();
		civil._save();
		index();
    }

	public static void update(long id) {
        List<Pays> pays = Pays.findAll();
        List<GenreSexuel> civilites = GenreSexuel.findAll();
        Civil civil = Civil.findById(id);
        String form = new Genform(civil, "/civil/update/"+id, "crudform").generate();
        render("CivilController/form.html", pays, civilites, form);
    }
	
	public static void postUpdate(@Valid Civil civil, long id) {
		civil = Civil.findById(id);
		civil.paysResidence = params.get("civil.paysResidence", Long.class) != -1 ? Pays.findById(params.get("civil.paysResidence", Long.class)) : null;
		civil.paysNatal = params.get("civil.paysNatal", Long.class) != -1 ? Pays.findById(params.get("civil.paysNatal", Long.class)) : null;
		civil.civilite = params.get("civil.civilite", Long.class) != -1 ? GenreSexuel.findById(params.get("civil.civilite", Long.class)) : null;
		civil.dateModification = new Date();
		if(validation.hasErrors()) {
			for(play.data.validation.Error error : validation.errors()) {
				Logger.info(error.getKey());
				Logger.info(error.message());
				Logger.info("-----------");
			}
            params.flash();
            validation.keep();
            update(civil.id);
        }
		civil._save();
		index();
    }
	
	public static void delete(long id) {
        Civil civil = Civil.findById(id);
        civil._delete();
		index();
	}
	
}
