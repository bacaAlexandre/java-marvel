package controllers;

import java.util.Date;
import java.util.List;

import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;
import models.Civil;
import models.Pays;
import models.GenreSexuel;
import models.Organisation;

@With(Registration.class)
public class OrganisationController extends Controller {
	
	@Check({"Civil"})
	public static void index() {
        List<Organisation> orgas = Organisation.findAll();
	    render(orgas);
	}
	
}
