package lib;

import java.util.List;
import java.util.Map;
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
import play.data.validation.Error;
import play.mvc.Scope.Flash;
import models.*;

public class Genform {
	
	private Object model;
	private String action;
	private String classes;
	
	final private String ERROR_CLASS = "erreur_input";
	
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
		return this.generate(null, null, "");
	}
	
	public String generate(Map<String, List<Error>> errors, Flash flash, String titre) {
		String form = "<form method=\"post\" action=\"";
		form += this.action + "\" class=\"" + this.classes + "\">";
		form += "<h2>"+titre+"</h2>";
		Class<?> classe = this.model.getClass();
		for (Field field : classe.getDeclaredFields()) {
	        field.setAccessible(true);
	        if (field.isAnnotationPresent(To_form.class)) {
	        	Boolean has_error = (errors != null && errors.containsKey(this.model.getClass().getSimpleName().toLowerCase() + "." + field.getName()));
	        	form += "<div><div>";
		        form += labelField(field);
        		String add_params = null;
        		if(flash != null && flash.get(this.model.getClass().getSimpleName().toLowerCase() + "." + field.getName()) != null) {
        			add_params = flash.get(this.model.getClass().getSimpleName().toLowerCase() + "." + field.getName());
        		}
	        	if (field.isAnnotationPresent(Column.class)) {
	        		boolean isTextArea = false;
	        		for(Annotation an : field.getDeclaredAnnotations()) {
		        		if(an.toString().contains("columnDefinition=text")) {
		        			isTextArea = true;
		        		}
	        		}
	        		if(isTextArea) {
		        		form += this.textareaField(field, add_params, has_error);
	        		} else {
		        		form += this.classicField(field, add_params, has_error);
	        		}
	        	} else if(field.getType().toString() == "Boolean") {
	        		form += this.booleanField(field, add_params, has_error);
	        	} else if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
	        		form += this.selectField(field, add_params, has_error);
	        	} else if (field.isAnnotationPresent(ManyToMany.class)) {
	        		form += this.selectMultipleField(field, add_params, has_error);
	        	}
	        	form += "</div>";
        		if(has_error) {
	        		form += "<span>" + errors.get(this.model.getClass().getSimpleName().toLowerCase() + "." + field.getName()).get(0).message() + "</span>";
	        	}
		        form += "</div>";
	        }
	    } 
		form += "<input type=\"submit\" value=\"Envoyer\" />";
		form += "</form>";
		return form;
	}
	
	private String classicField(Field field, String add_params, Boolean has_error) {
		String input = "<input " + setIdAndName(field, false);
		input += "type=\"";
		String type = "text";
		boolean isDate = false;
		switch(field.getType().toString()) {
			case "class java.util.Date":	
				type = "date";
				isDate = true;
				break;
			case "Integer":	
				type = "number";
				break;
			default: 		
				type = "text";
				break;
		}
		input += type + "\" ";
		
		try {
			if(add_params != null) {
				input += "value=\"" + (isDate ? add_params.substring(0, 10) : add_params) + "\" ";
			} else {
				final Field champ = this.model.getClass().getDeclaredField(field.getName());
				champ.setAccessible(true);
				String value = champ.get(this.model).toString();
				input += "value=\"" + (isDate ? value.substring(0, 10) : value) + "\" ";
			}
		} catch(Exception e) {
			Logger.error(e.toString());
		}
		if(has_error) {
			input += " class=\"" + ERROR_CLASS + "\" ";
		}
		input += " />";
		return input;
	}
	
	private String textareaField(Field field, String add_params, Boolean has_error) {
		String input = "<textarea " + setIdAndName(field, false);
		if(has_error) {
			input += " class=\"" + ERROR_CLASS + "\" ";
		}
		input += ">";
		try {
			if(add_params != null) {
				input += add_params;
			} else {
				final Field champ = this.model.getClass().getDeclaredField(field.getName());
				champ.setAccessible(true);
				input += champ.get(this.model).toString();
			}
		} catch(Exception e) {
			Logger.error(e.toString());
		}
		input += "</textarea>";
		return input;
	}
	
	private String booleanField(Field field, String add_params, Boolean has_error) {
		String input = "<input " + setIdAndName(field, false);
		try {
			if(add_params != null) {
				input += " checked ";
			} else {
				final Field champ = this.model.getClass().getDeclaredField(field.getName());
				champ.setAccessible(true);
				if(champ.get(this.model).toString() == "true") {
					input += " checked ";
				}
			}
		} catch(Exception e) {
			Logger.error(e.toString());
		}
		if(has_error) {
			input += " class=\"" + ERROR_CLASS + "\" ";
		}
		input += "type=\"checkbox\" />";
		return input;
	}
	
	private String selectField(Field field, String add_params, Boolean has_error) {
		String input = "<select " + setIdAndName(field, false);
		if(has_error) {
			input += " class=\"" + ERROR_CLASS + "\" ";
		}
		input += " >";
		input += "<option value=\"-1\"></option>";
		try {
			List liste = (List) field.getType().getMethod("findAll").invoke(field);
			for(Object obj : liste) {
				input += "<option value=\"" + obj.getClass().getMethod("getIdForDropdown").invoke(obj) + "\"";
				if(add_params != null && add_params != "-1" && obj.getClass().getMethod("getIdForDropdown").invoke(obj).toString().equals(add_params)) {
					input += " selected ";
				} else {
					final Field champ = this.model.getClass().getDeclaredField(field.getName());
					champ.setAccessible(true);
					if(obj == champ.get(this.model )) {
						input += " selected ";
					}
				}
				input += ">" + obj.getClass().getMethod("getNameForDropdown").invoke(obj) + "</option>";
			}
		} catch(Exception e) {
			Logger.error(e.toString());
		}
		
		input += "</select>";
		return input;
	}
	
	private String selectMultipleField(Field field, String add_params, Boolean has_error) {
		String input = "<select multiple " + setIdAndName(field, false);
		if(has_error) {
			input += " class=\"" + ERROR_CLASS + "\" ";
		}
		input += " >";
		try {
			ParameterizedType listType= (ParameterizedType) field.getGenericType();
			Class contentClass = (Class) listType.getActualTypeArguments()[0];
			List liste = (List) contentClass.getMethod("findAll").invoke(field);
			List base_list = (List) this.model.getClass().getDeclaredField(field.getName().toString()).get(this.model);
			for(Object obj : liste) {
				input += "<option value=\"" + obj.getClass().getMethod("getIdForDropdown").invoke(obj) + "\"";
				if(add_params != null && add_params != "-1" && obj.getClass().getMethod("getIdForDropdown").invoke(obj).toString().equals(add_params)) {
					input += " selected ";
				} else {
					final Field champ = this.model.getClass().getDeclaredField(field.getName());
					champ.setAccessible(true);
					if(base_list != null && base_list.contains(obj)) {
						input += " selected ";
					}
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
		String chaine = field.getName().replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
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
