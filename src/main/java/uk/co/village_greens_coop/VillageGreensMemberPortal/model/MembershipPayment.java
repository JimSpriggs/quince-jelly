package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "membership_payment")
@NamedQuery(name = MembershipPayment.FIND_BY_ID, query = "select mp from MembershipPayment mp where mp.id = :id")
public class MembershipPayment implements java.io.Serializable {

	public static final String FIND_BY_ID = "MembershipPayment.findById";

	private static final long serialVersionUID = -8972529419191664186L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "amount")
	private BigDecimal paymentAmount;

	@Column(name = "received_dt")
	private Date receivedDate;
	
	@Column(name = "due_dt")
	private Date dueDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "payment_method_cd")
	private PaymentMethod paymentMethod;

	
	public MembershipPayment() {
	}

	public MembershipPayment(Member member, BigDecimal paymentAmount,
			Date dueDate, Date receivedDate, PaymentMethod paymentMethod) {
		super();
		this.member = member;
		this.paymentAmount = paymentAmount;
		this.receivedDate = receivedDate;
		this.dueDate = dueDate;
		this.paymentMethod = paymentMethod;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
