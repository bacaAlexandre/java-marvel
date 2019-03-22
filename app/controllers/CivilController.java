package controllers;

import java.util.Date;
import java.util.List;

import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;
import models.Civil;
import models.Pays;
import models.GenreSexuel;

@With(Registration.class)
public class CivilController extends Controller {
	
	@Check({"Civil"})
	public static void index() {
        List<Civil> civils = Civil.findAll();
	    render(civils);
	}
	
	public static void newCivil() {
        List<Pays> pays = Pays.findAll();
        List<GenreSexuel> civilites = GenreSexuel.findAll();
        render(pays, civilites);
    }
	
	public static void addNewCivil(@Valid Civil civil) {
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            newCivil();
        }
		civil.dateAjout = new Date();
		civil._save();
		index();
    }
	
}
