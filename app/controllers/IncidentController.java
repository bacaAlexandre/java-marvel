package controllers;

import java.util.List;

import lib.Genform;
import models.Civil;
import models.Incident;
import models.TypeDelit;
import models.Utilisateur;
import play.data.validation.Valid;
import play.mvc.Controller;

public class IncidentController extends Controller {

	public static void declaration() {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("IncidentController", "create")) {
			String form = new Genform(new Incident(), "/incident/declaration", "crudform").generate(validation.errorsMap(), flash);
		    render("IncidentController/declaration.html", form);
		}
		redirect("/");
	}
	
	public static void declarer(@Valid Incident incident) {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("IncidentController", "create")) {
			/*try {
				incident.typeDelit = TypeDelit.findById(params.get("incident.typeDelit", Long.class));
				if(incident.typeDelit == null) {
					validation.addError("incident.typeDelit", "Required", "");
				}
			}
			catch{
				
			}*/
			incident.civil = utilisateur.civil;
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            declaration();
	        }
			incident.save();
		}
		declaration();
	}
}
