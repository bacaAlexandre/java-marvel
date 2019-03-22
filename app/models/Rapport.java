package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Rapport extends NewModel {

    @ManyToOne
    public Mission mission;

    @ManyToOne
    public Super affectation;

    @ManyToOne
    public Civil interlocuteur;

    @Column(nullable=false)
    public String resultat;

    @Column(columnDefinition = "text", nullable = false)
    public String degatCollateral;

}
