package controllers;

import java.util.List;

import lib.Genform;
import models.Civil;
import models.Incident;
import models.TypeDelit;
import play.data.validation.Valid;
import play.mvc.Controller;

public class IncidentController extends Controller {

	public static void declaration() {
		List<TypeDelit> delits = TypeDelit.findAll();
		String form = new Genform(new Incident(), "/incident/declaration", "crudform").generate(validation.errorsMap(), flash);
	    render("IncidentController/declaration.html", delits, form);
	}
	
	public static void declarer(@Valid Incident incident, long typeDelit) {
		incident.typeDelit = TypeDelit.findById(typeDelit);
		incident.civil = AuthController.getUtilisateurConnected().civil;
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            declaration();
        }
		incident._save();
        declaration();
	}
	
}
