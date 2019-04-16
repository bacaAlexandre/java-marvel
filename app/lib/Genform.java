package lib;

import java.util.List;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;

import play.Logger;

import models.*;

public class Genform {
	
	private Object model;
	private String action;
	private String classes;
	
	public Genform(Object object, String action) {
		this.model = object;
		this.action = action;
		this.classes = "";
	}
	
	public Genform(Object object, String action, String classes) {
		this.model = object;
		this.action = action;
		this.classes = classes;
	}
	
	public String generate() {
		String form = "<form method=\"post\" action=\"";
		form += this.action + "\" class=\"" + this.classes + "\">";
		Class<?> classe = this.model.getClass();
		for (Field field : classe.getDeclaredFields()) {
	        field.setAccessible(true);
	        if (field.isAnnotationPresent(To_form.class)) {
		        form += "<div>";
		        form += labelField(field);
	        	if (field.isAnnotationPresent(Column.class)) {
	        		form += this.classicField(field);
	        	} else if(field.getType().toString() == "Boolean") {
	        		form += this.booleanField(field);
	        	} else if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
	        		form += this.selectField(field);
	        	} else if (field.isAnnotationPresent(ManyToMany.class)) {
	        		form += this.selectMultipleField(field);
	        	}
		        form += "</div>";
	        }
	    } 
		form += "<input type=\"submit\" value=\"Envoyer\" />";
		form += "</form>";
		return form;
	}
	
	private String classicField(Field field) {
		String input = "<input " + setIdAndName(field, false);
		input += "type=\"";
		String type = "text";
		switch(field.getType().toString()) {
			case "Date":	type = "date";
							break;
			case "Integer":	type = "number";
							break;
			default: 		type = "text";
             				break;
		}
		input += type + "\" ";
		
		try {
			final Field champ = this.model.getClass().getDeclaredField(field.getName());
			champ.setAccessible(true);
			input += "value=\"" + champ.get(this.model).toString() + "\" ";
		} catch(Exception e) {
			Logger.error(e.toString());
		}
		
		input += " />";
		return input;
	}
	
	private String booleanField(Field field) {
		String input = "<input " + setIdAndName(field, false);
		try {
			final Field champ = this.model.getClass().getDeclaredField(field.getName());
			champ.setAccessible(true);
			if(champ.get(this.model).toString() == "true") {
				input += " checked ";
			}
		} catch(Exception e) {
			Logger.error(e.toString());
		}
		input += "type=\"checkbox\" />";
		return input;
	}
	
	private String selectField(Field field) {
		String input = "<select " + setIdAndName(field, false) + " >";
		input += "<option value=\"\"></option>";
		try {
			List liste = (List) field.getType().getMethod("findAll").invoke(field);
			for(Object obj : liste) {
				input += "<option value=\"" + obj.getClass().getMethod("getIdForDropdown").invoke(obj) + "\"";
				final Field champ = this.model.getClass().getDeclaredField(field.getName());
				champ.setAccessible(true);
				if(obj == champ.get(this.model )) {
					input += " selected ";
				}
				input += ">" + obj.getClass().getMethod("getNameForDropdown").invoke(obj) + "</option>";
			}
		} catch(Exception e) {
			Logger.error(e.toString());
		}
		
		input += "</select>";
		return input;
	}
	
	private String selectMultipleField(Field field) {
		String input = "<select multiple " + setIdAndName(field, false) + " >";
		try {
			ParameterizedType listType= (ParameterizedType) field.getGenericType();
			Class contentClass = (Class) listType.getActualTypeArguments()[0];
			List liste = (List) contentClass.getMethod("findAll").invoke(field);
			List base_list = (List) this.model.getClass().getDeclaredField(field.getName().toString()).get(this.model);
			for(Object obj : liste) {
				input += "<option value=\"" + obj.getClass().getMethod("getIdForDropdown").invoke(obj) + "\"";
				final Field champ = this.model.getClass().getDeclaredField(field.getName());
				champ.setAccessible(true);
				if(base_list.contains(obj)) {
					input += " selected ";
				}
				input += ">" + obj.getClass().getMethod("getNameForDropdown").invoke(obj) + "</option>";
			}
		} catch(Exception e) {
			Logger.error(e.toString());
		}
		
		input += "</select>";
		return input;
	}
	
	private String labelField(Field field) {
		String chaine = field.getName();
		String finalchaine = chaine.substring(0, 1).toUpperCase()+ chaine.substring(1).toLowerCase();
		return "<label " + setIdAndName(field, true) + ">" + finalchaine + " : </label>";
	}
	
	private String setIdAndName(Field field, boolean isLabel) {
		String thisName = this.model.getClass().getSimpleName().toLowerCase() + "." + field.getName();
		if(isLabel) {
			return "for=\"" + thisName + "\"";
		} else {
			return "name=\"" + thisName + "\" id=\"" + thisName + "\"";
		}
	}
	
}