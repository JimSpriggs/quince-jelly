package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Role;

@Repository
@Transactional(readOnly = true)
public class RoleDao {

	private static final Logger LOG = LoggerFactory.getLogger(RoleDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Role> getAll() {
		return (List<Role>)entityManager.createQuery("from Role r order by r.id")
			.getResultList();
	}
	
	public Role findByName(String name) {
		try {
			return entityManager.createNamedQuery(Role.FIND_BY_NAME, Role.class)
					.setParameter("name", name)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.error("Exception finding role {}", name, e);
			return null;
		}
	}
	
	@Transactional
	public Role save(Role role) {
		if (entityManager.contains(role)) {
			entityManager.merge(role);
		} 
		entityManager.persist(role);
		return role;
	}
}
