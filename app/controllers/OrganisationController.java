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
        List<Civil> civils = Civil.findAll();
        List<Pays> pays = Pays.findAll();
        String form = new Genform(new Organisation(), "").generate();
	    render(orgas, civils, pays, form);
	}
	
	public static void addOrga(@Valid Organisation organisation, @Required Long pays, @Required Long chef, @Required List<Long> membres) {
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            index();
        }
		organisation.dateAjout = new Date();
		organisation.pays = Pays.findById(pays);
		organisation.dirigeant = Civil.findById(chef);
		organisation.membres = Civil.getByIds(membres);
		organisation._save();
		index();
	}
	
	public static void toUpdate(Long id) {
		Organisation orga = Organisation.findById(id);
        List<Civil> civils = Civil.findAll();
        List<Pays> pays = Pays.findAll();
        String form = new Genform(orga, "").generate();
		render(orga, pays, civils, form);
	}
	
	public static void updateOrga(Long id) {
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
            toUpdate(id);
	    } else{
	    	organisation.save();
	    	index();
	    }
	}
	
	public static void deleteOrga(Long orga) {
		Utilisateur user = Utilisateur.find("byEmail", AuthController.connected()).<Utilisateur>first();
		Organisation organe = Organisation.find("byIdAndDirigeant", orga, user.civil ).<Organisation>first();
		organe._delete();
		redirect("/orga");
	}
	
}
