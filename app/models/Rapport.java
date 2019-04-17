package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lib.To_form;

@Entity
public class Rapport extends NewModel {

    @ManyToOne
    public Mission mission;

    @To_form
    @Column(nullable=false)
    public String resultat;

    @To_form
    @Column(columnDefinition = "text", nullable = false)
    public String degatCollateral;
    
    @To_form
    @ManyToOne
    public SurEtre affectation;

}
