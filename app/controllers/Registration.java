package controllers;

import java.util.Date;
import java.util.List;

import jdk.nashorn.internal.ir.RuntimeNode.Request;
import models.*;
import net.bytebuddy.agent.builder.AgentBuilder.InstallationListener.ErrorSuppressing;
import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.libs.Crypto;
import play.libs.Time;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;

public class Registration extends Controller {
	
    @Before(unless={"login", "authenticate", "register", "logout"})
    public static void checkAccess() throws Throwable {
        if(!session.contains("username")) {
            flash.put("url", "GET".equals(request.method) ? request.url : Play.ctxPath + "/"); // seems a good default
            login();
        }
        Check check = getActionAnnotation(Check.class);
        if(check != null) {
            check(check);
        }
        check = getControllerInheritedAnnotation(Check.class);
        if(check != null) {
            check(check);
        }
    }

    private static void check(Check check) throws Throwable {
        for(String profile : check.value()) {
            boolean hasProfile = check(profile);
            if(!hasProfile) {
                redirect("/");
            }
        }
    }

    public static void login() throws Throwable {
        Http.Cookie remember = request.cookies.get("rememberme");
        if(remember != null) {
            int firstIndex = remember.value.indexOf("-");
            int lastIndex = remember.value.lastIndexOf("-");
            if (lastIndex > firstIndex) {
                String sign = remember.value.substring(0, firstIndex);
                String restOfCookie = remember.value.substring(firstIndex + 1);
                String username = remember.value.substring(firstIndex + 1, lastIndex);
                String time = remember.value.substring(lastIndex + 1);
                Date expirationDate = new Date(Long.parseLong(time));
                Date now = new Date();
                if (expirationDate == null || expirationDate.before(now)) {
                    logout();
                }
                if(Crypto.sign(restOfCookie).equals(sign)) {
                    session.put("username", username);
                    redirectToOriginalURL();
                }
            }
        }
        flash.keep("url");
        List<Pays> pays = Pays.findAll();
        List<GenreSexuel> civilites = GenreSexuel.findAll();
        render(pays, civilites);
    }
    
    public static void register(@Valid Utilisateur utilisateur, @Valid Civil civil, @Required Long civilite, @Required Long paysResidence, @Required Long paysNatal, @Required String confirmPassword) throws Throwable {
    	if(validation.hasErrors()) {
    		flash.keep("url");
        	flash.error("secure.error");
        	validation.keep();
        	params.flash();
        	login();
    	}
    	civil.civilite = GenreSexuel.findById(civilite);
    	civil.paysNatal = Pays.findById(paysNatal);
    	civil.paysResidence = Pays.findById(paysResidence);
    	civil.dateAjout = new Date();
    	civil.save();
    	utilisateur.civil = civil;
    	utilisateur.save();
    	redirectToOriginalURL();
    }

    public static void authenticate(@Required String username, String password, boolean remember) throws Throwable {
        Boolean allowed = Utilisateur.connect(username, password) != null;
        if(validation.hasErrors() || !allowed) {
            flash.keep("url");
            flash.error("secure.error");
            params.flash();
            login();
        }
        session.put("username", username);
        if(remember) {
            Date expiration = new Date();
            String duration = Play.configuration.getProperty("secure.rememberme.duration","30d"); 
            expiration.setTime(expiration.getTime() + ((long)Time.parseDuration(duration)) * 1000L );
            response.setCookie("rememberme", Crypto.sign(username + "-" + expiration.getTime()) + "-" + username + "-" + expiration.getTime(), duration);

        }
        redirectToOriginalURL();
    }

    public static void logout() throws Throwable {
        session.clear();
        response.removeCookie("rememberme");
        flash.success("secure.logout");
        login();
    }

    public static void redirectToOriginalURL() throws Throwable {
        String url = flash.get("url");
        if(url == null) {
            url = Play.ctxPath + "/";
        }
        redirect(url);
    }
    
    public static String connected() {
        return session.get("username");
    }

    public static boolean isConnected() {
        return session.contains("username");
    }

    public static boolean check(String profile) {
    	Utilisateur user = Utilisateur.find("byEmail", connected()).<Utilisateur>first();
    	Droit getRight = Droit.find("byLibelle", profile).<Droit>first();
    	return user.droits.contains(getRight);
    }



}