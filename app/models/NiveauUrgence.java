package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="niveau_urgence")
public class NiveauUrgence extends NewModel {

    @Column(nullable=false)
    public String labelle;
}
