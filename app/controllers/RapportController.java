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
import play.Logger;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(AuthController.class)
public class RapportController extends Controller {

	public static void index() {
		Utilisateur utilisateur = AuthController.connected();
		RolePermission permission = utilisateur.getPermission("RapportController", "read");
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
		if (mission != null) {
			RolePermission permission = utilisateur.getPermission("RapportController", "create");
			if (utilisateur.isAdmin || permission != null) {
				if (utilisateur.isAdmin || permission.hasAll || mission.superHeros.stream().filter(s -> {
					return s.civil.id == utilisateur.civil.id;
				}).findFirst().isPresent()) {
					String form = new Genform(new Rapport(), "/rapport/add/" + id, "crudform").generate(validation.errorsMap(), flash, "Générer un rapport");
			        render("RapportController/form.html", form);
				}
			}
		}
		index();
    }
	
	public static void postCreate(@Valid Rapport rapport, long id) {
		Utilisateur utilisateur = AuthController.connected();
		Mission mission = Mission.findById(id);
		RolePermission permission = utilisateur.getPermission("RapportController", "create");
		if (utilisateur.isAdmin || permission != null) {
			if (utilisateur.isAdmin || permission.hasAll || mission.superHeros.stream().filter(s -> {
				return s.civil.id == utilisateur.civil.id;
			}).findFirst().isPresent()) {
				SurEtre superhero = SurEtre.findById(params.get("rapport.affectation", Long.class));
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
		}
		index();
	}
	
	public static void update(Long id) {
		if (id != null) {
			Utilisateur utilisateur = AuthController.connected();
			Rapport rapport = Rapport.findById(id);
			if (rapport != null && utilisateur.can("RapportController", "update", rapport.affectation.civil.id)) {
				String form = new Genform(rapport, "/rapport/update/"+id, "crudform").generate();
				render("RapportController/form.html", form);
			}
		}
		index();
	}
	
	public static void postUpdate(@Valid Rapport rapport, Long id) {
		if (id != null) {
			Utilisateur utilisateur = AuthController.connected();
			rapport = Rapport.findById(id);
			if (rapport != null && utilisateur.can("RapportController", "update", rapport.affectation.civil.id)) {
				SurEtre superhero = SurEtre.findById(params.get("rapport.affectation", Long.class));
				if(superhero == null) {
					validation.addError("rapport.affectation", "Required", "");
				}
				if(validation.hasErrors()) {
		            params.flash();
		            validation.keep();
		            update(id);
		        }
				rapport.edit(params.getRootParamNode(), "rapport");
				rapport.affectation = superhero;
				rapport.save();
			}
		}
		index();
	}
}
