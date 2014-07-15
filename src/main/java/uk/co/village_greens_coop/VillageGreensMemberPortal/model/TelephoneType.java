package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "telephone_type")
@NamedQuery(name = TelephoneType.FIND_BY_DESCRIPTION, query = "select tt from TelephoneType tt where tt.description = :description")
public class TelephoneType implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6737072063895112734L;

	public static final String FIND_BY_DESCRIPTION = "TelephoneType.findByDescription";

	@Id
	@Column(name = "telephone_type_cd")
	private String telephoneType;
	
	@Column
	private String description;

	public String getTelephoneType() {
		return telephoneType;
	}

	public void setTelephoneType(String telephoneType) {
		this.telephoneType = telephoneType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
