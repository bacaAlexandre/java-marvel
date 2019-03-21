package models;


import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Caracteristique extends NewModel {


    @Column(length=100, nullable = false)
    public String libelle;

    @Column(columnDefinition = "text")
    public String description;

    public Boolean isAvantage = false;
}
