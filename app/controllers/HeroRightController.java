package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class HeroRightController extends ConnectedController {

    public HeroRightController() {
    	if(! this.user.droits.contains(Droit.find("libelle = ?", "Héros"))) {
        	redirect("/");
        }
    }

}
