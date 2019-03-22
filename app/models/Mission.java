package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.sql.Date;
import java.util.List;

@Entity
public class Mission extends NewModel {

    @ManyToOne
    public Incident incident;

    @Column(nullable=false)
    public String titre;

    @ManyToOne
    @Column(nullable=false)
    public NatureMission nature;

    @Column(columnDefinition="datetime",nullable=false)
    public Date dateDebut;

    @Column(columnDefinition="text")
    public String commentaire;

    @ManyToOne
    public NiveauUrgence niveauUrgence = null;

    @ManyToOne
    @Column(nullable=false)
    public NiveauGravite niveauGravite;

    @ManyToMany
    @Column(nullable=false)
    public List<Super> superHeros;
}
