package controllers;

import java.util.List;

import play.mvc.Controller;

import models.Civil;

public class CivilController extends Controller {

	public static void index() {
        List<Civil> civils = Civil.findAll();
	    render(civils);
	}
	
	public static void newCivil() {
        render();
    }
	
	public static void addNewCivil(CivilController civil) {
        render();
    }
	
}
