package models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lib.To_form;
import play.data.validation.Required;

@Entity
public class Incident extends NewModel {

    @OneToOne
    public Mission mission;

	@To_form
    @Column(columnDefinition = "text", nullable = false)
    @Required
    public String lieu;

	@To_form
    @Column(columnDefinition = "text", nullable = false)
    @Required
    public String description;

	@To_form
    @ManyToOne
    public TypeDelit typeDelit;

    @ManyToOne
    public Civil civil = null;

    @ManyToOne
    public SurEtre superHeros = null;

    @ManyToOne
    public Organisation organisation = null;
}
