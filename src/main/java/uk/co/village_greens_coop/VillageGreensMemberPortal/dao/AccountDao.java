package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Account;

@Repository
@Transactional(readOnly = true)
public class AccountDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(AccountDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Account save(Account account) {
		if (entityManager.contains(account)) {
			entityManager.merge(account);
		} 
		entityManager.persist(account);
		return account;
	}
	
	public Account findByEmail(String email) {
		try {
			return entityManager.createNamedQuery(Account.FIND_BY_EMAIL, Account.class)
					.setParameter("email", email)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.warn("Failed to find account for email {}", email, e);
			return null;
		}
	}

	public Account findById(long id) {
		try {
			return entityManager.createNamedQuery(Account.FIND_BY_ID, Account.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.warn("Failed to find account with id {}", id, e);
			return null;
		}
	}

	
}
