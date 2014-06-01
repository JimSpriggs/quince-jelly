package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.AccountActivation;

@Repository
@Transactional
public class AccountActivationDao {

	private static final Logger LOG = LoggerFactory.getLogger(AccountActivationDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public AccountActivation save(AccountActivation accountActivation) {
		entityManager.persist(accountActivation);
		return accountActivation;
	}
	
	@Transactional
	public void delete(AccountActivation accountActivation) {
		entityManager.remove(accountActivation);
	}
	
	@Transactional(readOnly = true)
	public AccountActivation findByAccessKey(String accessKey) {
		try {
			return entityManager.createNamedQuery(AccountActivation.FIND_BY_ACCESS_KEY, AccountActivation.class)
					.setParameter("accessKey", accessKey)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.error("Exception finding account activation {}", accessKey, e);
			return null;
		}
	}
}
