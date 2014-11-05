package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Dashboard;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;

@Repository
@Transactional(readOnly = true)
public class DashboardDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Member> getAllEmailable() {
		return (List<Member>)entityManager.createQuery("from Member m where email IS NOT NULL m order by m.id")
				.getResultList();
	}

	public void getMemberCountsAndPayments(Dashboard dashboard) {
		String countsQuery = 
				"SELECT member_status_cd, "
				+ "(SELECT COUNT(*) FROM member WHERE member_status_cd = ms.member_status_cd) status_count, "
				+ "(SELECT SUM(amount) FROM membership_payment mp "
				+ 	"INNER JOIN member m ON m.id = mp.member_id "
				+ 	"WHERE m.member_status_cd = ms.member_status_cd "
				+ 	"AND mp.received_dt IS NOT NULL) status_amount, "
				+ 	"description "
				+ "FROM member_status ms";
		Query q = entityManager.createNativeQuery(countsQuery);
		@SuppressWarnings("rawtypes")
		List results = q.getResultList();
		if (results != null) {
			for (Object result: results) {
				Object[] row = (Object[]) result;
				String status = (String)row[0];
				BigInteger count = (BigInteger)row[1];
				BigDecimal total = (BigDecimal)row[2];
				if (total == null) {
					total = new BigDecimal("0.00");
				}
				
				if ("FULL".equals(status)) {
					dashboard.setFullMembers(count.intValue());
					dashboard.setPaidPledges(total);
				} else if ("PART".equals(status)) {
					dashboard.setPartMembers(count.intValue());
					dashboard.setPartPayments(total);
				} else if ("UNPAID".equals(status)) {
					dashboard.setUnpaidMembers(count.intValue());
					// unpaid members, by definition, have made no payments
				}
			}
		}
	}

	public void getTotalPledges(Dashboard dashboard) {
		String countsQuery = 
				"SELECT SUM(totalinvestment) FROM member";
		Query q = entityManager.createNativeQuery(countsQuery);
		Object row = q.getSingleResult();
		dashboard.setTotalPledges((BigDecimal)row);
	}

	public void getOverduePaymentsCount(Dashboard dashboard) {
		String countsQuery = 
				"SELECT SUM(amount), COUNT(*) FROM membership_payment WHERE due_dt < NOW() AND received_dt IS NULL";
		Query q = entityManager.createNativeQuery(countsQuery);
		Object[] row = (Object[]) q.getSingleResult();
		dashboard.setOverduePaymentsCount(((BigInteger)row[1]).intValue());
		// sum can return null
		if (row[0] != null) {
			dashboard.setOverduePayments((BigDecimal)row[0]);
		}
	}

	public void getOverdueMembersCount(Dashboard dashboard) {
		String countsQuery = 
				"SELECT COUNT(DISTINCT m.id) FROM member m INNER JOIN membership_payment mp ON m.id = mp.member_id "
				+ "WHERE mp.due_dt < NOW() AND mp.received_dt IS NULL";
		Query q = entityManager.createNativeQuery(countsQuery);
		BigInteger count = (BigInteger) q.getSingleResult();
		dashboard.setOverdueMembers(count.intValue());
	}

	public void getCertifiableMembersCount(Dashboard dashboard) {
		String countsQuery = 
				"SELECT COUNT(*) FROM member m "
						+ "WHERE m.member_status_cd = 'FULL' AND certificategenerated IS NULL";
		Query q = entityManager.createNativeQuery(countsQuery);
		BigInteger count = (BigInteger) q.getSingleResult();
		dashboard.setCertifiableMembers(count.intValue());
	}

	public void getStockEmailCount(Dashboard dashboard) {
		String countsQuery = 
				"SELECT COUNT(*) FROM stock_email se";
		Query q = entityManager.createNativeQuery(countsQuery);
		BigInteger count = (BigInteger) q.getSingleResult();
		dashboard.setStockEmailCount(count.intValue());
	}
}
