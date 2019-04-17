package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="nature_mission")
public class NatureMission extends NewModel {

    @Column(nullable=false)
    public String labelle;

    
    public String getNameForDropdown() {
		return this.labelle.toString();
	}
}
