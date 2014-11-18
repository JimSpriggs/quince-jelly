package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Document;

@Repository
@Transactional(readOnly = true)
public class DocumentDao {

	private static final Logger LOG = LoggerFactory.getLogger(DocumentDao.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Document> getAll() {
		return (List<Document>)entityManager.createQuery("from Document d order by d.creationTimestamp, d.id")
			.getResultList();
	}
	
	public Document findById(Long id) {
		try {
			return entityManager.createNamedQuery(Document.FIND_BY_ID, Document.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (PersistenceException e) {
			LOG.error("Exception finding Document with id {}", id, e);
			return null;
		}
	}
	
	@Transactional
	public Document save(Document document) {
		if (entityManager.contains(document)) {
			entityManager.merge(document);
		} 
		entityManager.persist(document);
		return document;
	}

	@Transactional
	public void delete(Document document) {
		entityManager.remove(document);
	}
	
}
