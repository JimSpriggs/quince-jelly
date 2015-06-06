package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MembershipPayment;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRow;

@Repository
@Transactional(readOnly = true)
public class MemberDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Member save(Member member) {
		if (entityManager.contains(member)) {
			entityManager.merge(member);
		} 
		entityManager.persist(member);
		return member;
	}
	
	public Member find(Serializable id) {
		return entityManager.find(Member.class,  id);
	}
	
	public Member findBySurname(String surname) {
		return entityManager.createNamedQuery(Member.FIND_BY_SURNAME, Member.class)
				.setParameter("surname", surname)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Member> getAll() {
		return (List<Member>)entityManager.createQuery("from Member m order by m.memberno, m.id")
				.getResultList();
	}

	public List<Member> getByMemberStatus(String memberStatus) {
		return (List<Member>)entityManager.createNamedQuery(Member.FIND_BY_STATUS, Member.class)
				.setParameter("memberStatus", memberStatus)
				.getResultList();
	}

	public List<Member> getCommitteeMembers() {
		return (List<Member>)entityManager.createNamedQuery(Member.FIND_COMMITTEE_MEMBERS, Member.class)
				.getResultList();
	}

	public List<Member> getCommitteeAndSysAdminMembers() {
		return (List<Member>)entityManager.createNamedQuery(Member.FIND_COMMITTEE_AND_SYSADMIN_MEMBERS, Member.class)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Member> getAllEmailable() {
		return (List<Member>)entityManager.createQuery("from Member m where email IS NOT NULL m order by m.id")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Member> getAllAwaitingCertificate() {
		return (List<Member>)entityManager.createQuery("from Member m where member_status_cd = 'FULL' and m.certificateGenerated IS NULL order by m.id")
//				.setFirstResult(0)
//				.setMaxResults(limit)
				.getResultList();
	}
	
	private MemberRow getOrCreateMemberRow(List<MemberRow> memberRows, Member member) {
		if (memberRows != null && memberRows.size() > 0) {
			for(MemberRow memberRow: memberRows) {
				if (memberRow.getId().equals(member.getId())) {
					return memberRow;
				}
			}
		}

		// member not found in list, so add it
		MemberRow mr = new MemberRow(member);
		memberRows.add(mr);
		return mr;
	}
	
	private List<MemberRow> getMemberRowsWithRelatedPaymentInfo(List<Member> members) {

		Date now = new Date();
		
		// get a list of all payments
		@SuppressWarnings("unchecked")
		List<MembershipPayment> payments = (List<MembershipPayment>)entityManager.createQuery("from MembershipPayment mp ORDER BY mp.member")
					.getResultList();

		List<MemberRow> memberRows = new ArrayList<MemberRow>();
		for (Member member: members) {
			memberRows.add(new MemberRow(member));
		}
		
		// now add payment values to the member rows
		if (payments != null && payments.size() > 0) {
			Member prevMember = null;
			MemberRow prevMemberRow = null;
			for (MembershipPayment payment: payments) {
				Member m = payment.getMember();
				if (m == prevMember || members.contains(m)) {
					MemberRow mr = null;
					if (m == prevMember) {
						mr = prevMemberRow;
					} else {
						// get or create the MemberRow
						mr = getOrCreateMemberRow(memberRows, m);
					}
					
					// add the amounts
					if (payment.getReceivedDate() == null &&
							payment.getDueDate() != null &&
							payment.getDueDate().before(now)) {
						mr.setAmountOverdue(mr.getAmountOverdue().add(payment.getPaymentAmount()));
					} else if (payment.getReceivedDate() != null) {
						mr.setAmountPaid(mr.getAmountPaid().add(payment.getPaymentAmount()));
					}
					// record the last member (and row) processed
					prevMember = m;
					prevMemberRow = mr;
				}
			}
		}
		
		return memberRows;
	}
	
	@SuppressWarnings("unchecked")
	public List<Member> getAllPossiblyOverdueMembers() {
		
		// get a list of members who could be overdue
		List<Member> members = (List<Member>)entityManager.createQuery("from Member m where member_status_cd IN ('UNPAID', 'PART')")
				.getResultList();

		return members;
	}
	
	public List<MemberRow> getOverdueMemberRows() {

		List<Member> members = getAllPossiblyOverdueMembers();
		
		List<MemberRow> rows = getMemberRowsWithRelatedPaymentInfo(members);
		List<MemberRow> newRows = new ArrayList<MemberRow>();
		
		// remove any which have a zero value of overdue
		BigDecimal zero = new BigDecimal(0);
		for(MemberRow row : rows) {
			if (row.getAmountOverdue().compareTo(zero) > 0) {
				newRows.add(row);
			}
		}
		
		return newRows;
	}
	
	public List<Member> getOverdueMembers() {
		List<Member> overdueMembers = new ArrayList<Member>();
		List<Member> possiblyOverdueMembers = getAllPossiblyOverdueMembers();
		List<MemberRow> overdueMemberRows = getOverdueMemberRows();
		
		if (overdueMemberRows.size() > 0) {
			for(MemberRow memberRow: overdueMemberRows) {
				Long memberRowId = memberRow.getId();
				// find the corresponding member object
				for (Member overdueMember: possiblyOverdueMembers) {
					if (overdueMember.getId().equals(memberRowId)) {
						// add it to the list
						overdueMembers.add(overdueMember);
						continue;
					}
				}
			}
		}
		
		return overdueMembers;
	}
	
	public List<Member> getFullMembers() {
		return getByMemberStatus("FULL");
	}
		
	public List<Member> getPartPaidMembers() {
		return getByMemberStatus("PART");
	}
		
	public List<Member> getUnpaidMembers() {
		return getByMemberStatus("UNPAID");
	}

	public List<MemberRow> getPartPaidMemberRows() {
		
		// get a list of members who could be overdue
		List<Member> members = getPartPaidMembers();

		return getMemberRowsWithRelatedPaymentInfo(members);
	}
	
	public List<MemberRow> getCommitteeMemberRows() {
		
		// get a list of members who could be overdue
		List<Member> members = getCommitteeMembers();

		return getMemberRowsWithRelatedPaymentInfo(members);
	}
	
	public List<MemberRow> getUnpaidMemberRows() {
		
		// get a list of members who could be overdue
		List<Member> members = getUnpaidMembers();

		return getMemberRowsWithRelatedPaymentInfo(members);
	}
	
	public Member generateMemberNoAndSave(Member member) {
		Query q = entityManager.createNativeQuery("SELECT nextval('member_no_seq')");
		BigInteger memberNo = (BigInteger)q.getSingleResult();
		member.setMemberno(memberNo.longValue());
		return save(member);
	}
}
