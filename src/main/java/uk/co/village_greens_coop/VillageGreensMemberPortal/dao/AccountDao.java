package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Account;

@Repository
@Transactional(readOnly = true)
public class AccountDao {
	
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
			return null;
		}
	}

	
}
