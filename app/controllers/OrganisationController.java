package controllers;

import java.util.Date;
import java.util.List;

import lib.Check;
import lib.Genform;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;
import models.Civil;
import models.Pays;
import models.Utilisateur;
import models.GenreSexuel;
import models.Organisation;

@With(AuthController.class)
public class OrganisationController extends Controller {
	
	@Check({"Civil"})
	public static void index() {
		List<Organisation> orgas = Organisation.findAll(); 
        render(orgas);
	}

	public static void create() {
        String form = new Genform(new Organisation(), "/orga/add", "crudform").generate(validation.errorsMap(), flash);
        render("OrganisationController/form.html", form);
    }
	
	public static void postCreate(@Valid Organisation organisation) {
		Long pays = params.get("organisation.pays", Long.class);
		Long dirigeant = params.get("organisation.dirigeant", Long.class);
		Long[] membres = params.get("organisation.membres", Long[].class);
		if(pays == -1) {
			validation.addError("organisation.pays", "Required", "");
		}
		if(dirigeant == -1) {
			validation.addError("organisation.dirigeant", "Required", "");
		}
		if(membres == null || membres.length <= 0) {
			validation.addError("organisation.membres", "Required", "");
		}
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            index();
        }
		organisation.dateAjout = new Date();
		organisation.pays = Pays.findById(pays);
		organisation.dirigeant = Civil.findById(dirigeant);
		organisation.membres = Civil.getByIds(params.data.get("organisation.membres"));
		organisation._save();
		index();
	}
	
	public static void update(Long id) {
		Organisation orga = Organisation.findById(id);
        String form = new Genform(orga, "/orga/update/"+id, "crudform").generate();
		render("OrganisationController/form.html", form);
	}
	
	public static void postUpdate(Long id) {
		Organisation organisation = Organisation.findById(id);
	    Long pays = params.get("organisation.pays", Long.class);
		Long dirigeant = params.get("organisation.dirigeant", Long.class);
		Long[] membres = params.get("organisation.membres", Long[].class);
		if(pays == -1) {
			validation.addError("organisation.pays", "Required", "");
		}
		if(dirigeant == -1) {
			validation.addError("organisation.dirigeant", "Required", "");
		}
		if(membres == null || membres.length <= 0) {
			validation.addError("organisation.membres", "Required", "");
		}
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            index();
        }
		organisation.dateModification = new Date();
		organisation.pays = Pays.findById(pays);
		organisation.dirigeant = Civil.findById(dirigeant);
		organisation.membres = Civil.getByIds(params.data.get("organisation.membres"));
		organisation._save();
		index();
	}
	
	public static void delete(Long orga) {
		Utilisateur user = Utilisateur.find("byEmail", AuthController.connected()).<Utilisateur>first();
		Organisation organe = Organisation.find("byIdAndDirigeant", orga, user.civil ).<Organisation>first();
		organe._delete();
    	index();
	}
	
}
