package models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="type_delit")
public class TypeDelit  extends NewModel {

    @Column(nullable=false)
    public String libelle;
}
