package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Caracteristique;
import models.Civil;
import models.GenreSexuel;
import models.Pays;
import models.Super;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(Registration.class)
public class VilainController extends Controller {
	
	public static void index() {
        List<Super> vilains = Super.getSuperType(false);
	    render(vilains);
	}
	
	public static void newVilain() {
		List<Caracteristique> caras = Caracteristique.findAll();
        List<Civil> civils = Civil.findAll();
        render(caras, civils);
    }
	
	public static void addNewVilain(@Valid Super vilain, Long civil) {
		if(validation.hasErrors()) {
			Logger.info(civil == null ? "zer" : civil.toString());
            params.flash();
            validation.keep();
            newVilain();
        }
		if(civil != null) {
			vilain.civil = Civil.findById(civil);
		}
		vilain._save();
		index();
    }

	public static void updateVilain(long id) {
		Super vilain = Super.findById(id);
        render(vilain);
    }
	
	public static void saveUpdateVilain(@Valid Super vilain, Long civil) {
		/*civil.paysResidence = Pays.findById(paysResidence);
		civil.paysNatal = Pays.findById(paysNatal);
		civil.civilite = GenreSexuel.findById(civilite);
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            updateCivil(civil.id);
        }
		civil.dateModification = new Date();
		civil._save();*/
		index();
    }
	
	public static void deleteVilain(long id) {
		Super vilain = Super.findById(id);
		vilain._delete();
		index();
	}
}
