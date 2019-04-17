package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lib.Genform;
import models.Civil;
import models.Mission;
import models.Organisation;
import models.Pays;
import models.Rapport;
import models.RolePermission;
import models.SurEtre;
import models.Utilisateur;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(AuthController.class)
public class RapportController extends Controller {

	public static void index() {
		Utilisateur utilisateur = AuthController.connected();
		RolePermission permission = utilisateur.getPermission("SuperHeroController", "read");
		if (utilisateur.isAdmin || permission != null) {
			List<Rapport> rapports = Rapport.findAll();
			if (!utilisateur.isAdmin && !permission.hasAll) {
				rapports = rapports.stream().filter(r -> {
					return r.affectation.civil.id == utilisateur.civil.id;	
				}).collect(Collectors.toList());
			}
			render(rapports);
		}
		redirect("/");
	}
	
	public static void create(long id) {
		Utilisateur utilisateur = AuthController.connected();
		Mission mission = Mission.findById(id);
		if (mission != null && utilisateur.can("OrganisationController", "create", id)) {
	        String form = new Genform(new Rapport(), "/rapport/add", "crudform").generate(validation.errorsMap(), flash);
	        render("RapportController/form.html", form);
		}
		index();
    }
	
	public static void postCreate(@Valid Rapport rapport, long id) {
		Utilisateur utilisateur = AuthController.connected();
		Mission mission = Mission.findById(id);
		if (mission != null && utilisateur.can("OrganisationController", "create", id)) {
			SurEtre superhero = SurEtre.findById(params.get("rapport.suretre", Long.class));
			if(superhero == null) {
				validation.addError("rapport.affectation", "Required", "");
			}
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            create(id);
	        }
			rapport.affectation = superhero;
			rapport.mission = mission;
			rapport.save();
			List<Rapport> rapports = Rapport.find("byMission", mission).fetch();
			if (rapports.size() >= mission.superHeros.size()) {
				mission.dateFin = new Date();
				mission.save();
			}
			
		}
		index();
	}
}
