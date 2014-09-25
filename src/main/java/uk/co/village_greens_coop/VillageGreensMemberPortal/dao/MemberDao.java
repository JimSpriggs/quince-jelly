package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;

@Repository
@Transactional(readOnly = true)
public class MemberDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Member save(Member member) {
		if (entityManager.contains(member)) {
			entityManager.merge(member);
		} 
		entityManager.persist(member);
		return member;
	}
	
	public Member find(Serializable id) {
		return entityManager.find(Member.class,  id);
	}
	
	public Member findBySurname(String surname) {
		return entityManager.createNamedQuery(Member.FIND_BY_SURNAME, Member.class)
				.setParameter("surname", surname)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Member> getAll() {
		return (List<Member>)entityManager.createQuery("from Member m order by m.memberno, m.id")
				.getResultList();
	}

	public List<Member> getByMemberStatus(String memberStatus) {
		return (List<Member>)entityManager.createNamedQuery(Member.FIND_BY_STATUS, Member.class)
				.setParameter("memberStatus", memberStatus)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Member> getAllEmailable() {
		return (List<Member>)entityManager.createQuery("from Member m where email IS NOT NULL m order by m.id")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Member> getAllAwaitingCertificate() {
		return (List<Member>)entityManager.createQuery("from Member m where member_status_cd = 'FULL' and m.certificateGenerated IS NULL order by m.id")
//				.setFirstResult(0)
//				.setMaxResults(limit)
				.getResultList();
	}
	
	public Member generateMemberNoAndSave(Member member) {
		Query q = entityManager.createNativeQuery("SELECT nextval('member_no_seq')");
		BigInteger memberNo = (BigInteger)q.getSingleResult();
		member.setMemberno(memberNo.longValue());
		return save(member);
	}
}
