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
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmailRequest;

@Repository
@Transactional
public class StockEmailRequestDao {

	private static final Logger LOG = LoggerFactory.getLogger(StockEmailRequestDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public StockEmailRequest save(StockEmailRequest stockEmailRequest) {
		if (entityManager.contains(stockEmailRequest)) {
			entityManager.merge(stockEmailRequest);
		} 
		entityManager.persist(stockEmailRequest);
		return stockEmailRequest;
	}
	
	@Transactional(readOnly = true)
	public List<StockEmailRequest> findUnsentBatch(int max) {
		return entityManager.createNamedQuery(StockEmailRequest.FIND_UNSENT, StockEmailRequest.class)
				.setMaxResults(max)
				.getResultList();
	}
}
