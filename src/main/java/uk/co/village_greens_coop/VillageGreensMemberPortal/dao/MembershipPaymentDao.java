package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MembershipPayment;

@Repository
@Transactional
public class MembershipPaymentDao {

	private static final Logger LOG = LoggerFactory.getLogger(MembershipPaymentDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void delete(MembershipPayment membershipPayment) {
		entityManager.remove(membershipPayment);
	}
	
	@Transactional
	public MembershipPayment save(MembershipPayment membershipPayment) {
		entityManager.persist(membershipPayment);
		return membershipPayment;
	}
	
	@Transactional(readOnly = true)
	public MembershipPayment findById(Long id) {
		try {
			return entityManager.createNamedQuery(MembershipPayment.FIND_BY_ID, MembershipPayment.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.error("Exception finding MembershipPayment {}", id, e);
			return null;
		}
	}
}
