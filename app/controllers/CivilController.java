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
	
	public static void newCivil() {
        List<Pays> pays = Pays.findAll();
        List<GenreSexuel> civilites = GenreSexuel.findAll();
        String form = new Genform(new Civil(), "/civil/add", "crudform").generate();
        render("CivilController/form.html", pays, civilites, form);
    }
	
	public static void addNewCivil(@Valid Civil civil, long paysResidence, long paysNatal, long civilite) {
		civil.paysResidence = Pays.findById(paysResidence);
		civil.paysNatal = Pays.findById(paysNatal);
		civil.civilite = GenreSexuel.findById(civilite);
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            newCivil();
        }
		civil.dateAjout = new Date();
		civil._save();
		index();
    }

	public static void updateCivil(long id) {
        List<Pays> pays = Pays.findAll();
        List<GenreSexuel> civilites = GenreSexuel.findAll();
        Civil civil = Civil.findById(id);
        String form = new Genform(civil, "/civil/update/"+id, "crudform").generate();
        render("CivilController/form.html", civil, pays, civilites, form);
    }
	
	/*public static void saveUpdateCivil(@Valid Civil civil, long paysResidence, long paysNatal, long civilite) {
		civil.paysResidence = Pays.findById(paysResidence);
		civil.paysNatal = Pays.findById(paysNatal);
		civil.civilite = GenreSexuel.findById(civilite);
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            updateCivil(civil.id);
        }
		civil.dateModification = new Date();
		civil._save();
		index();
    }*/
	
	public static void saveUpdateCivil(long id) {
		Civil civil = Civil.findById(id);
		civil.edit(params.getRootParamNode(), "civil");
		civil.paysResidence = Pays.findById(Long.parseLong(params.data.get("civil.paysResidence")[0]));
		civil.paysNatal = Pays.findById(Long.parseLong(params.data.get("civil.paysNatal")[0]));
		civil.civilite = GenreSexuel.findById(Long.parseLong(params.data.get("civil.civilite")[0]));
		civil.dateModification = new Date();
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            updateCivil(civil.id);
        }
		civil._save();
		index();
    }
	
	public static void deleteCivil(long id) {
        Civil civil = Civil.findById(id);
        civil._delete();
		index();
	}
	
}
