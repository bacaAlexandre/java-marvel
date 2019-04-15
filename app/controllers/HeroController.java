package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Civil;
import models.Super;
import play.mvc.Controller;
import play.mvc.With;

@With(Registration.class)
public class HeroController extends Controller {
	
	public static void index() {
        List<Super> heroes = Super.getSuperType(true);
	    render(heroes);
	}
}
