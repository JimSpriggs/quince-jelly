package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmail;

@Repository
@Transactional(readOnly = true)
public class StockEmailDao {

	private static final Logger LOG = LoggerFactory.getLogger(StockEmailDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public StockEmail save(StockEmail stockEmail) {
		if (entityManager.contains(stockEmail)) {
			entityManager.merge(stockEmail);
		} 
		entityManager.persist(stockEmail);
		return stockEmail;
	}
	
	@Transactional
	public void delete(StockEmail stockEmail) {
		entityManager.remove(stockEmail);
	}
	
	@SuppressWarnings("unchecked")
	public List<StockEmail> getAll() {
		return (List<StockEmail>)entityManager.createQuery("from StockEmail se order by se.id")
			.getResultList();
	}
	
	public StockEmail findByPurpose(String purpose) {
		try {
			return entityManager.createNamedQuery(StockEmail.FIND_BY_PURPOSE, StockEmail.class)
					.setParameter("emailPurpose", purpose)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.error("Exception finding StockEmail with purpose {}", purpose, e);
			return null;
		}
	}

	public StockEmail findById(Long id) {
		try {
			return entityManager.createNamedQuery(StockEmail.FIND_BY_ID, StockEmail.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.error("Exception finding StockEmail with id {}", id, e);
			return null;
		}
	}
}
