package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.ListSubscriber;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MemberTelephone;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MembershipPayment;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRow;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ListSubscriberDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public ListSubscriber save(ListSubscriber listSubscriber) {
		if (entityManager.contains(listSubscriber)) {
			entityManager.merge(listSubscriber);
		} 
		entityManager.persist(listSubscriber);
		return listSubscriber;
	}
	
	public ListSubscriber find(Serializable id) {
		return entityManager.find(ListSubscriber.class, id);
	}
	
	@Transactional(noRollbackFor = NoResultException.class)
	public ListSubscriber findByUuid(String uuid) {
		return entityManager.createNamedQuery(ListSubscriber.FIND_BY_UUID, ListSubscriber.class)
				.setParameter("uuid", uuid)
				.getSingleResult();
	}
}
