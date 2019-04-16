package controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lib.Genform;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;
import models.Civil;
import models.Pays;
import models.RolePermission;
import models.Utilisateur;
import models.GenreSexuel;
import models.Organisation;

@With(AuthController.class)
public class OrganisationController extends Controller {
	
	public static void index() {
		Utilisateur utilisateur = AuthController.connected();
		RolePermission permission = utilisateur.getPermission("OrganisationController", "read");
		if (utilisateur.isAdmin || permission != null) {
			List<Organisation> orgas = Organisation.findAll();
			if (!utilisateur.isAdmin && !permission.hasAll) {
				orgas = orgas.stream().filter(o -> {
					return o.dirigeant.id == utilisateur.civil.id;	
				}).collect(Collectors.toList());
			}
	        render(orgas);
		}
		redirect("/");
	}

	public static void create() {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("OrganisationController", "create")) {
	        String form = new Genform(new Organisation(), "/orga/add", "crudform").generate(validation.errorsMap(), flash);
	        render("OrganisationController/form.html", form);
		}
		index();
    }
	
	public static void postCreate(@Valid Organisation organisation) {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("OrganisationController", "create")) {
			Long pays = params.get("organisation.pays", Long.class);
			Long dirigeant = params.get("organisation.dirigeant", Long.class);
			Long[] membres = params.get("organisation.membres", Long[].class);
			if(pays == -1) {
				validation.addError("organisation.pays", "Required", "");
			}
			if(dirigeant == -1) {
				validation.addError("organisation.dirigeant", "Required", "");
			}
			if(membres == null || membres.length <= 0) {
				validation.addError("organisation.membres", "Required", "");
			}
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            index();
	        }
			organisation.dateAjout = new Date();
			organisation.pays = Pays.findById(pays);
			organisation.dirigeant = Civil.findById(dirigeant);
			organisation.membres = Civil.getByIds(params.data.get("organisation.membres"));
			organisation.save();
		}
		index();
	}
	
	public static void update(Long id) {
		Utilisateur utilisateur = AuthController.connected();
		Organisation orga = Organisation.findById(id);
		if (utilisateur.can("OrganisationController", "update", orga.dirigeant.id)) {
	        String form = new Genform(orga, "/orga/update/"+id, "crudform").generate();
			render("OrganisationController/form.html", form);
		}
		index();
	}
	
	public static void postUpdate(Long id) {
		Utilisateur utilisateur = AuthController.connected();
		Organisation organisation = Organisation.findById(id);
		if (utilisateur.can("OrganisationController", "update", organisation.dirigeant.id)) {
		    Long pays = params.get("organisation.pays", Long.class);
			Long dirigeant = params.get("organisation.dirigeant", Long.class);
			Long[] membres = params.get("organisation.membres", Long[].class);
			if(pays == -1) {
				validation.addError("organisation.pays", "Required", "");
			}
			if(dirigeant == -1) {
				validation.addError("organisation.dirigeant", "Required", "");
			}
			if(membres == null || membres.length <= 0) {
				validation.addError("organisation.membres", "Required", "");
			}
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            index();
	        }
			organisation.dateModification = new Date();
			organisation.pays = Pays.findById(pays);
			organisation.dirigeant = Civil.findById(dirigeant);
			organisation.membres = Civil.getByIds(params.data.get("organisation.membres"));
			organisation.save();
		}
		index();
	}
	
	public static void delete(Long id) {
		Utilisateur utilisateur = AuthController.connected();
		Organisation organisation = Organisation.findById(id);
		if (utilisateur.can("OrganisationController", "delete", organisation.dirigeant.id)) {
			organisation.delete();
		}
    	index();
	}
	
}
