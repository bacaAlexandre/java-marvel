package controllers;

import java.util.Date;
import java.util.List;

import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;
import models.Civil;
import models.Pays;
import models.GenreSexuel;
import models.Organisation;

@With(Registration.class)
public class OrganisationController extends Controller {
	
	@Check({"Civil"})
	public static void index() {
        List<Organisation> orgas = Organisation.findAll();
        List<Civil> civils = Civil.findAll();
        List<Pays> pays = Pays.findAll();
	    render(orgas, civils, pays);
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
		redirect("/orga");
	}
	
}
