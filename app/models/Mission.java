package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lib.To_form;

import java.sql.Date;
import java.util.List;

@Entity
public class Mission extends NewModel {

    @OneToOne
    public Incident incident;

    @To_form
    @Column(nullable=false)
    public String titre;

    @To_form
    @ManyToOne
    public NatureMission nature;

    @Column(columnDefinition="datetime",nullable=false)
    public Date dateDebut;

    @Column(columnDefinition="datetime")
    public Date dateFin;

    @To_form
    @Column(columnDefinition="text")
    public String commentaire;

    @To_form
    @ManyToOne
    public NiveauUrgence niveauUrgence = null;

    @To_form
    @ManyToOne
    public NiveauGravite niveauGravite;

    @To_form
    @ManyToMany
    public List<SurEtre> superHeros;
}
