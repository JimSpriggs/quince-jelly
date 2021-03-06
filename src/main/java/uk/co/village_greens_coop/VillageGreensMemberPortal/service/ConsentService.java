package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.DashboardDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.ListSubscriberConsentDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberConsentDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.*;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRow;

import java.util.Date;
import java.util.List;

@Service
public class ConsentService {

    private static final Logger LOG = LoggerFactory.getLogger(ConsentService.class);

    @Autowired
	private MemberService memberService;

    @Autowired
    private MemberConsentDao memberConsentDao;

    @Autowired
    private ListSubscriberService listSubscriberService;

    @Autowired
    private ListSubscriberConsentDao listSubscriberConsentDao;

    @Transactional(noRollbackFor = RuntimeException.class)
	public boolean memberConsent(String uuid) {
	    Member member = memberService.getByUuid(uuid);
	    if (member != null) {
	        LOG.debug("Member {} has given consent for emails", member.getMemberno());
	        for (MemberConsent consent: member.getMemberConsents()) {
	            consent.setMarketing(true);
	            consent.setUpdateTimestamp(new Date());
	            memberConsentDao.save(consent);
            }
            return true;
        } else {
	        return false;
        }
    }

    @Transactional(noRollbackFor = RuntimeException.class)
    public boolean memberUnsubscribe(String uuid) {
        Member member = memberService.getByUuid(uuid);
        if (member != null) {
            LOG.debug("Member {} has requested to unsubscribe from emails", member.getMemberno());
            for (MemberConsent consent: member.getMemberConsents()) {
                consent.setMarketing(false);
                consent.setUpdateTimestamp(new Date());
                memberConsentDao.save(consent);
            }
            return true;
        } else {
            return false;
        }
    }

    @Transactional(noRollbackFor = RuntimeException.class)
    public boolean listSubscriberConsent(String uuid) {
        ListSubscriber listSubscriber = listSubscriberService.getByUuid(uuid);
        if (listSubscriber != null) {
            LOG.debug("ListSubscriber {} has given consent for emails", listSubscriber.getId());
            for (ListSubscriberConsent consent: listSubscriber.getListSubscriberConsents()) {
                consent.setMarketing(true);
                consent.setUpdateTimestamp(new Date());
                listSubscriberConsentDao.save(consent);
            }
            return true;
        } else {
            return false;
        }
    }

    @Transactional(noRollbackFor = RuntimeException.class)
    public boolean listSubscriberUnsubscribe(String uuid) {
        ListSubscriber listSubscriber = listSubscriberService.getByUuid(uuid);
        if (listSubscriber != null) {
            LOG.debug("ListSubscriber {} has requested to unsubscribe from emails", listSubscriber.getId());
            for (ListSubscriberConsent consent: listSubscriber.getListSubscriberConsents()) {
                consent.setMarketing(false);
                consent.setUpdateTimestamp(new Date());
                listSubscriberConsentDao.save(consent);
            }
            return true;
        } else {
            return false;
        }
    }
}
