package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import java.util.Date;
import java.util.List;

@Entity
public class Mission extends NewModel {

    @OneToOne
    public Incident incident;

    @Column(nullable=false)
    public String titre;

    @ManyToOne
    public NatureMission nature;

    @Column(columnDefinition="datetime",nullable=false)
    public Date dateDebut;

    @Column(columnDefinition="datetime")
    public Date dateFin;

    @Column(columnDefinition="text")
    public String commentaire;

    @ManyToOne
    public NiveauUrgence niveauUrgence = null;

    @ManyToOne
    public NiveauGravite niveauGravite;

    @ManyToMany
    public List<SurEtre> superHeros;
}
