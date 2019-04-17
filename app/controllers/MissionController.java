package controllers;

import java.util.List;

import lib.Genform;
import models.Mission;
import models.Organisation;
import models.Utilisateur;
import play.mvc.Controller;
import play.data.validation.Valid;

public class MissionController extends Controller {

	private static Utilisateur utilisateur = AuthController.connected();
	
	public static void index() {
		
	}

	public static void transform(Long id_incident) {
		if (utilisateur.can("MissionController", "create")) {
	        String form = new Genform(new Mission(), "/mission/add/"+id_incident, "crudform").generate(validation.errorsMap(), flash);
	        render("MissionController/form.html", form);
		}
		index();
	}
	
	public static void add(@Valid Mission mission, Long id_incident) {
		
	}
	
}
