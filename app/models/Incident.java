package models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Incident extends NewModel {

    @Column(columnDefinition = "text", nullable = false)
    public String lieu;

    @Column(columnDefinition = "text", nullable = false)
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
