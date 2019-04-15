package controllers;

import java.util.List;

import models.Civil;
import models.Incident;
import models.TypeDelit;
import play.data.validation.Valid;
import play.mvc.Controller;

public class IncidentController extends Controller {

	public static void declaration() {
		List<TypeDelit> delits = TypeDelit.findAll();
	    render(delits);
	}
	
	public static void declarer(@Valid Incident incident, long typeDelit) {
		incident.typeDelit = TypeDelit.findById(typeDelit);
		if(validation.hasErrors()) {
            params.flash();
            validation.keep();
            declaration();
        }
		incident._save();
        declaration();
	}
	
}
