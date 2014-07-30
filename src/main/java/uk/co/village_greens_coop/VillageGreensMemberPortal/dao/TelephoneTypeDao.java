package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.TelephoneType;

@Repository
@Transactional
public class TelephoneTypeDao {

	private static final Logger LOG = LoggerFactory.getLogger(TelephoneTypeDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional(readOnly = true)
	public TelephoneType findByType(String type) {
		try {
			return entityManager.createNamedQuery(TelephoneType.FIND_BY_TYPE, TelephoneType.class)
					.setParameter("telephoneType", type)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.error("Exception finding TelephoneType {}", type, e);
			return null;
		}
	}
}
