package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Utilisateur extends Model {

	public String login;

	public String password;

}
