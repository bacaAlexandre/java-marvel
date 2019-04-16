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
        /*for(play.data.validation.Error errro : validation.errors()) {
        		Logger.info(errro.getKey());
        		Logger.info(errro.message());
        		Logger.info("----------------------");
    	}*/
        String form = new Genform(new Civil(), "/civil/add", "crudform").generate(validation.errorsMap(), flash);
        render("CivilController/form.html", pays, civilites, form);
    }
	
	public static void postCreate(@Valid Civil civil) {
		Long paysResidenceID = params.get("civil.paysResidence", Long.class);
		Long paysNatalID = params.get("civil.paysNatal", Long.class);
		Long civiliteID = params.get("civil.civilite", Long.class);
		if(paysResidenceID == -1) {
			validation.addError("civil.paysResidence", "Required", "");
		}
		if(paysNatalID == -1) {
			validation.addError("civil.paysNatal", "Required", "");
		}
		if(civiliteID == -1) {
			validation.addError("civil.civilite", "Required", "");
		}
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            create();
        }
		civil.paysResidence = Pays.findById(paysResidenceID);
		civil.paysNatal = Pays.findById(paysNatalID);
		civil.civilite = GenreSexuel.findById(civiliteID);
		civil.dateAjout = new Date();
		civil._save();
		index();
    }

	public static void update(long id) {
        List<Pays> pays = Pays.findAll();
        List<GenreSexuel> civilites = GenreSexuel.findAll();
        Civil civil = Civil.findById(id);
        String form = new Genform(civil, "/civil/update/"+id, "crudform").generate(validation.errorsMap(), flash);
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
