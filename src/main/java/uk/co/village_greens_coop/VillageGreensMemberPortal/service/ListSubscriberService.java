package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.*;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.MemberForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.PaymentForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.TelephoneForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.ListSubscriber;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MemberTelephone;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MembershipPayment;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRow;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListSubscriberService {

	private static final Logger LOG = LoggerFactory.getLogger(ListSubscriberService.class);
	
	@Autowired
	private CertificateService certificateService;
	
	@Autowired
	private ListSubscriberDao listSubscriberRepository;

	@PostConstruct
	protected void initialize() {
	}
	
	@Transactional(readOnly = true)
	public ListSubscriber getByUuid(String uuid) {
		try {
			return listSubscriberRepository.findByUuid(uuid);
		} catch (RuntimeException e) {
			LOG.error("Exception caught when attempting to find list subscriber with uuid {}", uuid, e);
		}
		return null;
	}
}
