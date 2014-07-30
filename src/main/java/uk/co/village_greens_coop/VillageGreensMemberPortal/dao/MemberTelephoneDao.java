package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MemberTelephone;

@Repository
@Transactional
public class MemberTelephoneDao {

	private static final Logger LOG = LoggerFactory.getLogger(MemberTelephoneDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void delete(MemberTelephone memberTelephone) {
		entityManager.remove(memberTelephone);
	}
	
	@Transactional
	public MemberTelephone save(MemberTelephone memberTelephone) {
		entityManager.persist(memberTelephone);
		return memberTelephone;
	}
	
	@Transactional(readOnly = true)
	public MemberTelephone findById(Long id) {
		try {
			return entityManager.createNamedQuery(MemberTelephone.FIND_BY_ID, MemberTelephone.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.error("Exception finding MemberTelephone {}", id, e);
			return null;
		}
	}
}
