package models;


import play.db.jpa.Model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Desavantage extends NewModel {


    @Column(length=100, nullable = false)
    public String libelle;

    @Column(columnDefinition = "text")
    public String description;

	public String getNameForDropdown() {
		return this.libelle.toString();
	}
}
