package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.PasswordReset;

@Repository
@Transactional
public class PasswordResetDao {

	private static final Logger LOG = LoggerFactory.getLogger(PasswordResetDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void delete(PasswordReset passwordReset) {
		entityManager.remove(passwordReset);
	}
	
	@Transactional
	public PasswordReset save(PasswordReset passwordReset) {
		entityManager.persist(passwordReset);
		return passwordReset;
	}
	
	@Transactional(readOnly = true)
	public PasswordReset findByAccessKey(String accessKey) {
		try {
			return entityManager.createNamedQuery(PasswordReset.FIND_BY_ACCESS_KEY, PasswordReset.class)
					.setParameter("accessKey", accessKey)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.error("Exception finding password reset {}", accessKey, e);
			return null;
		}
	}

	@Transactional(readOnly = true)
	public List<PasswordReset> findAllExpired(Date cutOffDate) {
		try {
			return entityManager.createNamedQuery(PasswordReset.FIND_ALL_EXPIRED, PasswordReset.class)
					.setParameter("cutOffDate", cutOffDate)
					.getResultList();
		} catch (PersistenceException e) {
			LOG.error("Exception finding expired password resets with cut-off date {}", cutOffDate, e);
			return null;
		}
	}
}
