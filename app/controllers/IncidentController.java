package controllers;

import java.util.List;

import lib.Genform;
import models.Civil;
import models.Incident;
import models.Organisation;
import models.Pays;
import models.TypeDelit;
import models.Utilisateur;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.Controller;

public class IncidentController extends Controller {

	public static void index() {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("IncidentController", "read")) {
			List<Incident> incidents = Incident.find("byMissionIsNull").fetch();
			render("IncidentController/index.html", incidents);
		}
		redirect("/");
	}

	public static void declaration() {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("IncidentController", "create")) {
			List<TypeDelit> delits = TypeDelit.findAll();
			String form = new Genform(new Incident(), "/incident/declaration", "crudform")
					.generate(validation.errorsMap(), flash);
			render("IncidentController/declaration.html", delits, form);
		}
		redirect("/");
	}

	public static void declarer(@Valid Incident incident) {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("IncidentController", "create")) {
			incident.typeDelit = TypeDelit.findById(params.get("incident.typeDelit", Long.class));
			if(incident.typeDelit == null) {
				validation.addError("incident.typeDelit", "Required", "");
			}
			if (validation.hasErrors()) {
				params.flash();
				validation.keep();
				declaration();
			}
			incident.civil = utilisateur.civil;
			incident.save();
			if(!utilisateur.isHero()) {
				Logger.info("win");
		    	flash.success("Votre déclaration a été prise en compte.");
				redirect("/");
			} else {
				Logger.info("lose");
				index();
			}
		}
		declaration();
	}
	
	public static void delete(Long id) {
		if (id != null) {
			Utilisateur utilisateur = AuthController.connected();
			Incident incident = Incident.findById(id);
			if (incident != null && utilisateur.can("IncidentController", "delete")) {
				incident.delete();
			}
		}
    	index();
	}
}
