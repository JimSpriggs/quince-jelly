package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "stock_email_request")
@NamedQueries({
	@NamedQuery(name = StockEmailRequest.FIND_UNSENT, query = "select r from StockEmailRequest r where r.sentTimestamp IS NULL AND r.error IS NULL ORDER BY r.id")
})
public class StockEmailRequest implements java.io.Serializable {

	public static final String FIND_UNSENT = "StockEmailRequest.findUnsent";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "request_ts")
	private Date requestTimestamp;
	@Column(name = "sent_ts")
	private Date sentTimestamp;
	@Column(name = "recipient_email_tx")
	private String recipientEmail;
	@Column(name = "error_tx")
	private String error;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private Member member;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "list_subscriber_id")
	private ListSubscriber listSubscriber;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "stock_email_id")
	private StockEmail stockEmail;
	
	public StockEmailRequest() {
	}

	public StockEmailRequest(final Member member, final StockEmail stockEmail) {
		this.member = member;
		this.stockEmail = stockEmail;
		this.requestTimestamp = new Date();
	}

	public StockEmailRequest(final ListSubscriber listSubscriber, final StockEmail stockEmail) {
		this.listSubscriber = listSubscriber;
		this.stockEmail = stockEmail;
		this.requestTimestamp = new Date();
	}

	public StockEmailRequest(final StockEmail stockEmail, final String recipientEmail) {
		this.member = null;
		this.stockEmail = stockEmail;
		this.recipientEmail = recipientEmail;
		this.requestTimestamp = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(Date requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}

	public Date getSentTimestamp() {
		return sentTimestamp;
	}

	public void setSentTimestamp(Date sentTimestamp) {
		this.sentTimestamp = sentTimestamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public ListSubscriber getListSubscriber() {
		return listSubscriber;
	}

	public void setListSubscriber(ListSubscriber listSubscriber) {
		this.listSubscriber = listSubscriber;
	}

	public StockEmail getStockEmail() {
		return stockEmail;
	}

	public void setStockEmail(StockEmail stockEmail) {
		this.stockEmail = stockEmail;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}
	
}
