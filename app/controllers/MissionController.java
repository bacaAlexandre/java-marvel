package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lib.Genform;
import models.Civil;
import models.Incident;
import models.Mission;
import models.NatureMission;
import models.NiveauGravite;
import models.NiveauUrgence;
import models.Organisation;
import models.Pays;
import models.SurEtre;
import models.Utilisateur;
import play.mvc.Controller;
import play.data.validation.Valid;

public class MissionController extends Controller {

	private static Utilisateur utilisateur = AuthController.connected();
	
	public static void index() {
		render("MissionController/index.html");
	}

	public static void transform(Long id_incident) {
		if (utilisateur.can("MissionController", "create")) {
	        String form = new Genform(new Mission(), "/mission/add/"+id_incident, "crudform").generate(validation.errorsMap(), flash);
	        render("MissionController/form.html", form);
		}
		index();
	}
	
	public static void add(@Valid Mission mission, Long id_incident) {
		if (utilisateur.can("MissionController", "create")) {
			Incident incident = Incident.findById(id_incident);
			mission.incident = incident;
			mission.nature = NatureMission.findById(params.get("mission.nature", Long.class));
			mission.niveauUrgence = NiveauUrgence.findById(params.get("mission.niveauUrgence", Long.class));
			mission.niveauGravite = NiveauGravite.findById(params.get("mission.niveauGravite", Long.class));
			if(mission.incident == null) {
				validation.addError("mission.incident", "Required", "");
			}
			if(mission.nature == null) {
				validation.addError("mission.nature", "Required", "");
			}
			if(mission.niveauUrgence == null) {
				validation.addError("mission.niveauUrgence", "Required", "");
			}
			if(mission.niveauGravite == null) {
				validation.addError("mission.niveauGravite", "Required", "");
			}
			Long[] supers_id = params.get("mission.superHeros", Long[].class);
			List<SurEtre> superHeros = new ArrayList<>();
			if (supers_id != null) {
				superHeros.addAll(SurEtre.find("id in (?1)", Arrays.asList(supers_id)).fetch());
			}
			if(superHeros.isEmpty()) {
				validation.addError("mission.superHeros", "Required", "");
			}
			mission.superHeros = superHeros;
			mission.dateDebut = new Date();
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            transform(id_incident);
	        }
			Mission new_mission = mission.save();
			incident.mission = new_mission;
			incident.save();
		}
		index();
	}
	
}
