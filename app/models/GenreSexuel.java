package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="genre_sexuel")
public class GenreSexuel extends NewModel {

	@Column(nullable=false)
	public String libelle;
	
}