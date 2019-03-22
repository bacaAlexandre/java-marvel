package controllers;

import java.util.List;

import play.mvc.Controller;

import models.Civil;
import models.Pays;
import models.GenreSexuel;

public class CivilController extends Controller {

	public static void index() {
        List<Civil> civils = Civil.findAll();
	    render(civils);
	}
	
	public static void newCivil() {
        List<Pays> pays = Pays.findAll();
        List<GenreSexuel> civilites = GenreSexuel.findAll();
        render(pays, civilites);
    }
	
	public static void addNewCivil(Civil civil) {
        render();
    }
	
}
