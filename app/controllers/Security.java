package controllers;

import models.*;

public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {
        return Utilisateur.connect(username, password) != null;
    }

}