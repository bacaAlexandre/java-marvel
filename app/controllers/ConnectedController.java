package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;


@With(Secure.class)
public class ConnectedController extends Controller {

	protected static Utilisateur user;
	
    public ConnectedController() {
    	this.user = (Utilisateur) new Utilisateur().find("email = ?", Security.connected()).fetch();
    }

}
