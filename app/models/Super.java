package models;


import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import java.util.List;

@Entity
public class Super extends NewModel {

    @Column(length = 100, nullable = false)
    public String nom;

    @Column(columnDefinition = "text")
    public String commentaire;

    public Boolean isHero = false;

    @ManyToMany
    public List<Caracteristique> avantage;

    @ManyToMany
    public List<Caracteristique> desavantage;

}
