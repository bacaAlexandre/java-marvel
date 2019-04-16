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
        String form;
        if(validation.hasErrors()) {
        	form = new Genform(new Organisation(), "/orga/add", "crudform").generate(validation.errorsMap(), flash);
        } else {
        	form = new Genform(new Organisation(), "/orga/add", "crudform").generate();
        }
        render(form);
	}
	
	public static void postCreate(@Valid Organisation organisation) {
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            index();
        }
		organisation.dateAjout = new Date();
		organisation.pays = params.data.get("organisation.pays") != null ? Pays.findById(Long.parseLong(params.data.get("organisation.pays")[0])) : null;
		organisation.dirigeant = params.data.get("organisation.dirigeant") != null ? Civil.findById(Long.parseLong(params.data.get("organisation.dirigeant")[0])) : null;
		organisation.membres = params.data.get("organisation.membres") != null ? Civil.getByIds(params.data.get("organisation.membres")) : null;
		organisation._save();
		index();
	}
	
	public static void update(Long id) {
		Organisation orga = Organisation.findById(id);
        List<Civil> civils = Civil.findAll();
        List<Pays> pays = Pays.findAll();
        String form = new Genform(orga, "/orga/update/"+id, "crudform").generate();
		render(orga, pays, civils, form);
	}
	
	public static void postUpdate(Long id) {
		Organisation organisation = Organisation.findById(id);
		organisation.edit(params.getRootParamNode(), "organisation");
		organisation.pays = Pays.findById(Long.parseLong(params.data.get("pays")[0]));
		organisation.dirigeant = Civil.findById(Long.parseLong(params.data.get("chef")[0]));
		organisation.membres = Civil.getByIds(params.data.get("membres"));
		organisation.dateModification = new Date();
	    validation.valid(organisation);
	    if(validation.hasErrors()) {
	    	params.flash();
            validation.keep();
            update(id);
	    } else{
	    	organisation.save();
	    	index();
	    }
	}
	
	public static void delete(Long orga) {
		Utilisateur user = Utilisateur.find("byEmail", AuthController.connected()).<Utilisateur>first();
		Organisation organe = Organisation.find("byIdAndDirigeant", orga, user.civil ).<Organisation>first();
		organe._delete();
    	index();
	}
	
}
