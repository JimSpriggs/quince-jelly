package uk.co.village_greens_coop.VillageGreensMemberPortal.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.ContactList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Repository
@Transactional
public class ContactListDao {
    private static final Logger LOG = LoggerFactory.getLogger(ContactListDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public ContactList findById(Long id) {
        try {
            return entityManager.createNamedQuery(ContactList.FIND_BY_LIST, ContactList.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (PersistenceException e) {
            LOG.error("Exception finding ContactList {}", id, e);
            return null;
        }
    }}
