package models;

import org.hibernate.annotations.GenericGenerator;
import play.db.jpa.GenericModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class NewModel extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    public Long id;

    public Long getId() {
        return id;
    }

    @Override
    public Object _key() {
        return getId();
    }
	
	public String getIdForDropdown() {
		return this.id.toString();
	}

}