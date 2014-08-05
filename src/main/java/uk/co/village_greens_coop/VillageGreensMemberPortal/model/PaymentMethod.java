package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "payment_method")
@NamedQuery(name = PaymentMethod.FIND_BY_METHOD, query = "select pm from PaymentMethod pm where pm.paymentMethod = :paymentMethod")
public class PaymentMethod implements java.io.Serializable {

	private static final long serialVersionUID = 7428057770882912707L;

	public static final String FIND_BY_METHOD = "PaymentMethod.findBymethod";

	@Id
	@Column(name = "payment_method_cd")
	private String paymentMethod;
	
	@Column
	private String description;

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
