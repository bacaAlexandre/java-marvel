package models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class GenreSexuel extends NewModel {

	@Column(nullable=false)
	public String libelle;
	
}