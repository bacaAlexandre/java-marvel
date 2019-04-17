package controllers;

import java.util.ArrayList;
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
import models.Caracteristique;
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
			Pays pays = Pays.findById(params.get("organisation.pays", Long.class));
			Civil dirigeant = Civil.findById(params.get("organisation.dirigeant", Long.class));
			Long[] membres_id = params.get("organisation.membres", Long[].class);
			List<Civil> membres = new ArrayList<>();
			if (membres_id != null) {
				membres.addAll(Civil.find("id in (?1)", Arrays.asList(membres_id)).fetch());
			}
			if(pays == null) {
				validation.addError("organisation.pays", "Required", "");
			}
			if(dirigeant == null) {
				validation.addError("organisation.dirigeant", "Required", "");
			}
			if(membres.isEmpty()) {
				validation.addError("organisation.membres", "Required", "");
			}
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            create();
	        }
			organisation.dateAjout = new Date();
			organisation.pays = pays;
			organisation.dirigeant = dirigeant;
			organisation.membres = membres;
			organisation.save();
		}
		index();
	}
	
	public static void update(Long id) {
		if (id != null) {
			Utilisateur utilisateur = AuthController.connected();
			Organisation orga = Organisation.findById(id);
			if (orga != null && utilisateur.can("OrganisationController", "update", orga.dirigeant.id)) {
		        String form = new Genform(orga, "/orga/update/"+id, "crudform").generate();
				render("OrganisationController/form.html", form);
			}
		}
		index();
	}
	
	public static void postUpdate(Long id) {
		if (id != null) {
			Utilisateur utilisateur = AuthController.connected();
			Organisation organisation = Organisation.findById(id);
			if (utilisateur.can("OrganisationController", "update", organisation.dirigeant.id)) {
			    Pays pays = Pays.findById(params.get("organisation.pays", Long.class));
				Civil dirigeant = Civil.findById(params.get("organisation.dirigeant", Long.class));
				Long[] membres_id = params.get("organisation.membres", Long[].class);
				List<Civil> membres = new ArrayList<>();
				if (membres_id != null) {
					membres.addAll(Civil.find("id in (?1)", Arrays.asList(membres_id)).fetch());
				}
				if(pays == null) {
					validation.addError("organisation.pays", "Required", "");
				}
				if(dirigeant == null) {
					validation.addError("organisation.dirigeant", "Required", "");
				}
				if(membres.isEmpty()) {
					validation.addError("organisation.membres", "Required", "");
				}
				if(validation.hasErrors()) {
		            params.flash();
		            validation.keep();
		            index();
		        }
				organisation.edit(params.getRootParamNode(), "organisation");
				organisation.dateModification = new Date();
				organisation.pays = pays;
				organisation.dirigeant = dirigeant;
				organisation.membres = membres;
				organisation.save();
			}
		}
		index();
	}
	
	public static void delete(Long id) {
		if (id != null) {
			Utilisateur utilisateur = AuthController.connected();
			Organisation organisation = Organisation.findById(id);
			if (organisation != null && utilisateur.can("OrganisationController", "delete", organisation.dirigeant.id)) {
				organisation.delete();
			}
		}
    	index();
	}
	
}
