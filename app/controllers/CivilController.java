package controllers;

import java.util.List;

import play.mvc.Controller;
import play.mvc.With;
import models.Civil;

@With(Registration.class)
public class CivilController extends Controller {
	
	@Check({"civil", "test"})
	public static void index() {
        List<Civil> civils = Civil.findAll();
	    render(civils);
	}
	
	public static void newCivil() {
        render();
    }
	
	public static void addNewCivil(Civil civil) {
        render();
    }
	
}
