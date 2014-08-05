package uk.co.village_greens_coop.VillageGreensMemberPortal.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

//@ScriptAssertFieldError.List({
//	@ScriptAssertFieldError(
//			lang = "javascript",
//			script = "!_this.updateState.equals('N') || ( _this.updateState.equals('N') && ( ( _this.telephoneNumber == '' && _this.telephoneType == '' ) || ( _this.telephoneNumber != '' ) ) ) ",
//			fieldName = "telephoneNumber"),
//	@ScriptAssertFieldError(
//			lang = "javascript",
//			script = "!_this.updateState.equals('N') || ( _this.updateState.equals('N') && ( ( _this.telephoneNumber == '' && _this.telephoneType == '' ) || ( _this.telephoneType != '' ) ) ) ",
//			fieldName = "telephoneType"),
//	@ScriptAssertFieldError(
//			lang = "javascript",
//			script = "!_this.updateState.equals('U') || ( _this.updateState.equals('U') && _this.telephoneNumber != '' )",
//			fieldName = "telephoneNumber"),
//	@ScriptAssertFieldError( // 
//			lang = "javascript",
//			script = "!_this.updateState.equals('U') || ( _this.updateState.equals('U') && _this.telephoneType != '' )",
//			fieldName = "telephoneType")
//})
public class PaymentForm implements Comparable<PaymentForm> {

	private Long id;
	
	@Digits(integer=5, fraction=2)
	@Min(1)
	private BigDecimal amount;
	
	private String paymentMethod;
	private Date dueDate;
	private Date receivedDate;
	
	@Pattern(regexp = "[CUND]")
	private String updateState = "N";
	
	public PaymentForm() {
	}
	
	public PaymentForm(Long id, BigDecimal amount, String paymentMethod, Date dueDate, Date receivedDate) {
		super();
		this.updateState = "U";
		this.id = id;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.dueDate = dueDate;
		this.receivedDate = receivedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getUpdateState() {
		return updateState;
	}

	public void setUpdateState(String updateState) {
		this.updateState = updateState;
	}

	@Override
	public int compareTo(PaymentForm o) {
		int result = 0;
		
		// primarily, sorting should be on received date...
		if (receivedDate != null) {
			if  (o.receivedDate == null) {
				result = -1;
			} else {
				result = receivedDate.compareTo(o.receivedDate);
			}
		} else if (o.receivedDate != null) {
			result = 1;
		}
		
		// ...but if received dates are both null, or both equal, use due date 
		if (result != 0) {
			return result;
		}
		
		if (dueDate != null) {
			if  (o.dueDate == null) {
				result = -1;
			} else {
				result = dueDate.compareTo(o.dueDate);
			}
		} else if (o.dueDate != null) {
			result = 1;
		}
		
		return result;
	}
}
