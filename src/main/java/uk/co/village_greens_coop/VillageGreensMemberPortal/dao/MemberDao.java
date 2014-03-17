package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;

@Repository
public class MemberDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Member save(Member member) {
		entityManager.persist(member);
		return member;
	}
	
	public Member find(Serializable memberNo) {
		return entityManager.find(Member.class,  memberNo);
	}
	public Member findBySurname(String surname) {
		return entityManager.createNamedQuery(Member.FIND_BY_SURNAME, Member.class)
				.setParameter("surname", surname)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Member> getAll() {
		return (List<Member>)entityManager.createQuery("from Member")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Member> getAllEmailable() {
		return (List<Member>)entityManager.createQuery("from Member where email IS NOT NULL")
				.getResultList();
	}
}
