package uk.co.village_greens_coop.VillageGreensMemberPortal.model;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class Dashboard implements java.io.Serializable {

	private int allMembers;
	private int fullMembers;
	private int partMembers;
	private int unpaidMembers;
	private int overdueMembers;
	private int certifiableMembers;
	private BigDecimal totalPledges = new BigDecimal("0.00");
	private BigDecimal paidPledges = new BigDecimal("0.00");
	private BigDecimal partPayments = new BigDecimal("0.00");
	private BigDecimal overduePayments = new BigDecimal("0.00");
	private int overduePaymentsCount;
	private int stockEmailCount;
	private int documentCount;

	public Dashboard() {
	}

    public int getOverduePaymentsCount() {
		return overduePaymentsCount;
	}

	public void setOverduePaymentsCount(int overduePaymentsCount) {
		this.overduePaymentsCount = overduePaymentsCount;
	}

	public int getAllMembers() {
		return allMembers;
	}

	public void setAllMembers(int allMembers) {
		this.allMembers = allMembers;
	}

	public int getFullMembers() {
		return fullMembers;
	}

	public void setFullMembers(int fullMembers) {
		this.fullMembers = fullMembers;
	}

	public int getPartMembers() {
		return partMembers;
	}

	public void setPartMembers(int partMembers) {
		this.partMembers = partMembers;
	}

	public int getUnpaidMembers() {
		return unpaidMembers;
	}

	public void setUnpaidMembers(int unpaidMembers) {
		this.unpaidMembers = unpaidMembers;
	}

	public BigDecimal getTotalPledges() {
		return totalPledges;
	}

	public void setTotalPledges(BigDecimal totalPledges) {
		this.totalPledges = totalPledges;
	}

	public BigDecimal getPaidPledges() {
		return paidPledges;
	}

	public void setPaidPledges(BigDecimal paidPledges) {
		this.paidPledges = paidPledges;
	}

	public BigDecimal getPartPayments() {
		return partPayments;
	}

	public void setPartPayments(BigDecimal partPayments) {
		this.partPayments = partPayments;
	}

	public BigDecimal getOverduePayments() {
		return overduePayments;
	}

	public void setOverduePayments(BigDecimal overduePayments) {
		this.overduePayments = overduePayments;
	}

	public int getOverdueMembers() {
		return overdueMembers;
	}

	public void setOverdueMembers(int overdueMembers) {
		this.overdueMembers = overdueMembers;
	}

	public int getCertifiableMembers() {
		return certifiableMembers;
	}

	public void setCertifiableMembers(int certifiableMembers) {
		this.certifiableMembers = certifiableMembers;
	}

	public int getStockEmailCount() {
		return stockEmailCount;
	}

	public void setStockEmailCount(int stockEmailCount) {
		this.stockEmailCount = stockEmailCount;
	}

	public int getDocumentCount() {
		return documentCount;
	}

	public void setDocumentCount(int documentCount) {
		this.documentCount = documentCount;
	}
	
}
