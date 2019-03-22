package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class CivilRightController extends ConnectedController {

    public CivilRightController() {
        if(! this.user.droits.contains(Droit.find("libelle = ?", "Civil"))) {
        	redirect("/");
        }
    }

}
