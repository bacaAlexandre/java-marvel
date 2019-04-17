package controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lib.Genform;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;
import models.Civil;
import models.Pays;
import models.RolePermission;
import models.SurEtre;
import models.Utilisateur;
import models.GenreSexuel;
import models.Organisation;

@With(AuthController.class)
public class CivilController extends Controller {
	
	public static void index() {
		Utilisateur utilisateur = AuthController.connected();
		RolePermission permission = utilisateur.getPermission("CivilController", "read");
		if (utilisateur.isAdmin || permission != null) {
			List<Civil> civils = Civil.findAll();
			if (!utilisateur.isAdmin && !permission.hasAll) {
				civils = civils.stream().filter(c -> {
					return c.id == utilisateur.civil.id;	
				}).collect(Collectors.toList());
			}
			render(civils);
		}
		redirect("/");
	}
	
	public static void create() {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("CivilController", "create")) {
	        String form = new Genform(new Civil(), "/civil/add", "crudform").generate(validation.errorsMap(), flash);
	        render("CivilController/form.html", form);
		}
		index();
    }
	
	public static void postCreate(@Valid Civil civil) {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("CivilController", "create")) {
			Long paysResidenceID = params.get("civil.paysResidence", Long.class);
			Long paysNatalID = params.get("civil.paysNatal", Long.class);
			Long civiliteID = params.get("civil.civilite", Long.class);
			if(paysResidenceID == -1) {
				validation.addError("civil.paysResidence", "Required", "");
			}
			if(paysNatalID == -1) {
				validation.addError("civil.paysNatal", "Required", "");
			}
			if(civiliteID == -1) {
				validation.addError("civil.civilite", "Required", "");
			}
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            create();
	        }
			civil.paysResidence = Pays.findById(paysResidenceID);
			civil.paysNatal = Pays.findById(paysNatalID);
			civil.civilite = GenreSexuel.findById(civiliteID);
			civil.dateAjout = new Date();
			civil.save();
		}
		index();
    }

	public static void update(long id) {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("CivilController", "update", id)) {
	        Civil civil = Civil.findById(id);
	        String form = new Genform(civil, "/civil/update/"+id, "crudform").generate(validation.errorsMap(), flash);
	        render("CivilController/form.html", form);
		}
		index();
    }

	public static void postUpdate(@Valid Civil civil, long id) {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("CivilController", "update", id)) {
			civil = Civil.findById(id);
			Long paysResidenceID = params.get("civil.paysResidence", Long.class);
			Long paysNatalID = params.get("civil.paysNatal", Long.class);
			Long civiliteID = params.get("civil.civilite", Long.class);
			if(paysResidenceID == -1) {
				validation.addError("civil.paysResidence", "Required", "");
			}
			if(paysNatalID == -1) {
				validation.addError("civil.paysNatal", "Required", "");
			}
			if(civiliteID == -1) {
				validation.addError("civil.civilite", "Required", "");
			}
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            create();
	        }
			civil.edit(params.getRootParamNode(), "civil");
			civil.paysResidence = Pays.findById(paysResidenceID);
			civil.paysNatal = Pays.findById(paysNatalID);
			civil.civilite = GenreSexuel.findById(civiliteID);
			civil.dateModification = new Date();
			civil.save();
		}
		index();
    }

	public static void delete(long id) {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("CivilController", "delete", id)) {
	        Civil civil = Civil.findById(id);
	        civil.delete();
		}
		index();
	}
	
}
