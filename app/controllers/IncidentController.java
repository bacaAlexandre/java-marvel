package controllers;

import java.util.List;

import lib.Genform;
import models.Civil;
import models.Incident;
import models.Organisation;
import models.TypeDelit;
import models.Utilisateur;
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

	public static void declarer(@Valid Incident incident, long typeDelit) {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("IncidentController", "create")) {
			incident.typeDelit = TypeDelit.findById(typeDelit);
			incident.civil = utilisateur.civil;
			if (validation.hasErrors()) {
				params.flash();
				validation.keep();
				declaration();
			}
			incident.save();
		}
		declaration();
	}
}
