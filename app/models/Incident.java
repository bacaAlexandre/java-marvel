package models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.data.validation.Required;

@Entity
public class Incident extends NewModel {

    @OneToOne
    public Mission mission;

    @Column(columnDefinition = "text", nullable = false)
    @Required
    public String lieu;

    @Column(columnDefinition = "text", nullable = false)
    @Required
    public String description;

    @ManyToOne
    public TypeDelit typeDelit;

    @ManyToOne
    public Civil civil = null;

    @ManyToOne
    public Super superHeros = null;

    @ManyToOne
    public Organisation organisation = null;
}
