package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.PaymentMethod;

@Repository
@Transactional
public class PaymentMethodDao {

	private static final Logger LOG = LoggerFactory.getLogger(PaymentMethodDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional(readOnly = true)
	public PaymentMethod findByMethod(String method) {
		try {
			return entityManager.createNamedQuery(PaymentMethod.FIND_BY_METHOD, PaymentMethod.class)
					.setParameter("paymentMethod", method)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.error("Exception finding TelephoneType {}", method, e);
			return null;
		}
	}
}
