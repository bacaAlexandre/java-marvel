package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

@With(AuthController.class)
public class HomeController extends Controller {

    public static void index() {
		Utilisateur utilisateur = AuthController.connected();
		if (utilisateur.can("HomeController", "read")) {
			render(utilisateur);
		}
        forbidden();
    }

}