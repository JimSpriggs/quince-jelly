package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.ListSubscriberConsent;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MemberConsent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional(readOnly = true)
public class ListSubscriberConsentDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public ListSubscriberConsent save(ListSubscriberConsent listSubscriberConsent) {
		if (entityManager.contains(listSubscriberConsent)) {
			entityManager.merge(listSubscriberConsent);
		} 
		entityManager.persist(listSubscriberConsent);
		return listSubscriberConsent;
	}

}
