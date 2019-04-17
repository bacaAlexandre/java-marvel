package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lib.Genform;
import models.Caracteristique;
import models.Civil;
import models.GenreSexuel;
import models.Pays;
import models.RolePermission;
import models.SurEtre;
import models.Utilisateur;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(AuthController.class)
public class VilainController extends Controller {
	
	public static void index() {
		Utilisateur utilisateur = AuthController.connected();
		RolePermission permission = utilisateur.getPermission("VilainController", "read");
		if (utilisateur.isAdmin || permission != null) {
			 List<SurEtre> vilains = SurEtre.getSurEtreType(false);
			if (!utilisateur.isAdmin && !permission.hasAll) {
				vilains = vilains.stream().filter(v -> {
					return v.civil.id == utilisateur.civil.id;	
				}).collect(Collectors.toList());
			}
			render(vilains);
		}
		redirect("/");
	}
	
	public static void create() {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("VilainController", "create")) {
	        String form = new Genform(new SurEtre(), "/vilain/add", "crudform").generate(validation.errorsMap(), flash);
	        render("VilainController/form.html", form);
		}
		index();
    }
	
	public static void postCreate(@Valid SurEtre suretre) {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("VilainController", "create")) {
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            create();
	        }
			suretre.civil = Civil.findById(params.get("suretre.civil", Long.class));
			Long[] avantages = params.get("suretre.avantages", Long[].class);
			Long[] desavantages = params.get("suretre.desavantages", Long[].class);
			if (avantages != null) {
				suretre.avantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(avantages)).fetch());
			}
			if (desavantages != null) {
				suretre.desavantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(desavantages)).fetch());
			}
			suretre.isHero = false;
			suretre.save();
		}
		index();
    }

	public static void update(long id) {
		Utilisateur utilisateur = AuthController.connected();
		SurEtre vilain = SurEtre.findById(id);
		if (utilisateur.can("VilainController", "update")) {
	        String form = new Genform(vilain, "/vilain/update/"+id, "crudform").generate(validation.errorsMap(), flash);
	        render("VilainController/form.html", vilain, form);
		}
		index();
	}
	
	public static void postUpdate(long id) {
		Utilisateur utilisateur = AuthController.connected();
		SurEtre vilain = SurEtre.findById(id);
		if (utilisateur.can("VilainController", "update")) {
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            update(id);
	        }
			Long[] avantages = params.get("suretre.avantages", Long[].class);
			Long[] desavantages = params.get("suretre.desavantages", Long[].class);
			if (avantages != null) {
				vilain.avantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(avantages)).fetch());
			}
			if (desavantages != null) {
				vilain.desavantages.addAll(Caracteristique.find("id in (?1)", Arrays.asList(desavantages)).fetch());
			}
			vilain.edit(params.getRootParamNode(), "suretre");
			vilain.civil = Civil.findById(params.get("suretre.civil", Long.class));
			vilain.save();
		}
		index();
    }
	
	public static void delete(long id) {
		Utilisateur utilisateur = AuthController.connected();
		SurEtre vilain = SurEtre.findById(id);
		if (utilisateur.can("VilainController", "delete")) {
			vilain.delete();
		}
		index();
	}
}
