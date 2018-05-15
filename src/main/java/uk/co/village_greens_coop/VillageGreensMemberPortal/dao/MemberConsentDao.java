package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MemberConsent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional(readOnly = true)
public class MemberConsentDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public MemberConsent save(MemberConsent memberConsent) {
		if (entityManager.contains(memberConsent)) {
			entityManager.merge(memberConsent);
		} 
		entityManager.persist(memberConsent);
		return memberConsent;
	}

}
