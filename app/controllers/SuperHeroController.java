package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lib.Genform;
import models.Avantage;
import models.Desavantage;
import models.Civil;
import models.GenreSexuel;
import models.Organisation;
import models.Pays;
import models.RolePermission;
import models.SurEtre;
import models.Utilisateur;
import play.Logger;
import play.data.validation.Error;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(AuthController.class)
public class SuperHeroController extends Controller {

	public static void index() {
		Utilisateur utilisateur = AuthController.connected();
		RolePermission permission = utilisateur.getPermission("SuperHeroController", "read");
		if (utilisateur.isAdmin || permission != null) {
			List<SurEtre> superheros = SurEtre.getSurEtreType(true);
			if (!utilisateur.isAdmin && !permission.hasAll) {
				superheros = superheros.stream().filter(s -> {
					return s.civil.id == utilisateur.civil.id;	
				}).collect(Collectors.toList());
			}
			render(superheros);
		}
		redirect("/");
	}
	
	public static void create() {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("SuperHeroController", "create")) {
	        String form = new Genform(new SurEtre(), "/superhero/add", "crudform").generate(validation.errorsMap(), flash, "Ajouter un SuperHero");
	        render("SuperHeroController/form.html", form);
		}
        index();
    }
	
	public static void postCreate(@Valid SurEtre suretre) {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("SuperHeroController", "create")) {
			suretre.civil = Civil.findById(params.get("suretre.civil", Long.class));
			if(suretre.civil == null) {
				validation.addError("suretre.civil", "Required", "");
			}
			if(validation.hasErrors()) {
	            params.flash();
	            validation.keep();
	            create();
	        }
			Long[] avantages = params.get("suretre.avantages", Long[].class);
			Long[] desavantages = params.get("suretre.desavantages", Long[].class);
			
			List<Avantage> avantages_list = new ArrayList<>();
			List<Desavantage> desavantages_list = new ArrayList<>();
			
			if (avantages != null) {
				avantages_list.addAll(Avantage.find("id in (?1)", Arrays.asList(avantages)).fetch());
			}
			if (desavantages != null) {
				desavantages_list.addAll(Desavantage.find("id in (?1)", Arrays.asList(desavantages)).fetch());
			}
			if (avantages_list.isEmpty()) {
				validation.addError("suretre.avantages", "Required", "");
			}
			if (desavantages_list.isEmpty()) {
				validation.addError("suretre.desavantages", "Required", "");
			}
			suretre.avantages = avantages_list;
			suretre.desavantages = desavantages_list;
			suretre.isHero = true;
			suretre.save();
		}
		index();
    }
	
	public static void update(Long id) {
		if(id != null) {
			Utilisateur utilisateur = AuthController.connected();
			SurEtre suretre = SurEtre.findById(id);
			if (utilisateur.can("SuperHeroController", "update", suretre.civil.id)) {
		        String form = new Genform(suretre, "/superhero/update/"+id, "crudform").generate(validation.errorsMap(), flash, "Modifier informations " + suretre.nom);
		        render("SuperHeroController/form.html", form);
			}
		}
		index();
	}
	
	public static void postUpdate(@Valid SurEtre suretre, Long id) {
		if(id != null) {
			Utilisateur utilisateur = AuthController.connected();
			suretre = SurEtre.findById(id);
			if (utilisateur.can("SuperHeroController", "update", suretre.civil.id)) {
				suretre.edit(params.getRootParamNode(), "suretre");
				suretre.civil = Civil.findById(params.get("suretre.civil", Long.class));
				if(suretre.civil == null) {
					validation.addError("suretre.civil", "Required", "");
				}
				if(validation.hasErrors()) {
		            params.flash();
		            validation.keep();
		            update(id);
		        }
				Long[] avantages = params.get("suretre.avantages", Long[].class);
				Long[] desavantages = params.get("suretre.desavantages", Long[].class);
				
				List<Avantage> avantages_list = new ArrayList<>();
				List<Desavantage> desavantages_list = new ArrayList<>();
				
				if (avantages != null) {
					avantages_list.addAll(Avantage.find("id in (?1)", Arrays.asList(avantages)).fetch());
				}
				if (desavantages != null) {
					desavantages_list.addAll(Desavantage.find("id in (?1)", Arrays.asList(desavantages)).fetch());
				}
				if (avantages_list.isEmpty()) {
					validation.addError("suretre.avantages", "Required", "");
				}
				if (desavantages_list.isEmpty()) {
					validation.addError("suretre.desavantages", "Required", "");
				}
				suretre.avantages = avantages_list;
				suretre.desavantages = desavantages_list;
				suretre.save();
			}
		}
		index();
    }
	
	public static void delete(Long id) {
		if(id != null) {
			Utilisateur utilisateur = AuthController.connected();
			SurEtre superhero = SurEtre.findById(id);
			if (utilisateur.can("SuperHeroController", "delete", superhero.civil.id)) {
				superhero.delete();
			}
		}
		index();
	}
}
