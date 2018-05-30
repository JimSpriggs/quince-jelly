package uk.co.village_greens_coop.VillageGreensMemberPortal.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.AccountDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.ConsentService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.SignUpService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.UserService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.signup.SignupForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.support.web.MessageHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("email")
public class EmailConsentController {

	private static final Logger LOG = LoggerFactory.getLogger(EmailConsentController.class);
	private static final String MEMBER_CONSENT_VIEW_NAME = "email/member-consent";
	private static final String LIST_SUBSCRIBER_CONSENT_VIEW_NAME = "email/list-subscriber-consent";
	private static final String MEMBER_UNSUBSCRIBE_VIEW_NAME = "email/member-unsubscribe";
	private static final String LIST_SUBSCRIBER_UNSUBSCRIBE_VIEW_NAME = "email/list-subscriber-unsubscribe";
	private static final String CONSENT_ERROR_VIEW_NAME = "email/consent-error";
	private static final String UNSUBSCRIBE_ERROR_VIEW_NAME = "email/unsubscribe-error";

	@Autowired
	private ConsentService consentService;

	@RequestMapping(value = {"c/m/{uuid}", "c/{uuid}"})
	public String memberConsent(HttpSession session, Model model, @PathVariable("uuid") String uuid) {
		LOG.info("New consent request detected");
		// don't want a session to linger any longer than necessary, using up server resources
		session.invalidate();
		if (consentService.memberConsent(uuid)) {
			return MEMBER_CONSENT_VIEW_NAME;
		} else {
			return CONSENT_ERROR_VIEW_NAME;
		}
	}

	@RequestMapping(value = {"u/m/{uuid}", "u/{uuid}"})
	public String memberUnsubscribe(HttpSession session, Model model, @PathVariable("uuid") String uuid) {
		LOG.info("New unsubscribe request detected");
		session.invalidate();
		if (consentService.memberUnsubscribe(uuid)) {
			model.addAttribute("memberUuid", uuid);
			return MEMBER_UNSUBSCRIBE_VIEW_NAME;
		} else {
			return UNSUBSCRIBE_ERROR_VIEW_NAME;
		}
	}

	@RequestMapping(value = "c/l/{uuid}")
	public String listSubscriberConsent(HttpSession session, Model model, @PathVariable("uuid") String uuid) {
		LOG.info("New consent request detected");
		// don't want a session to linger any longer than necessary, using up server resources
		session.invalidate();
		if (consentService.listSubscriberConsent(uuid)) {
			return LIST_SUBSCRIBER_CONSENT_VIEW_NAME;
		} else {
			return CONSENT_ERROR_VIEW_NAME;
		}
	}

	@RequestMapping(value = "u/l/{uuid}")
	public String listSubscriberUnsubscribe(HttpSession session, Model model, @PathVariable("uuid") String uuid) {
		LOG.info("New unsubscribe request detected");
		session.invalidate();
		if (consentService.listSubscriberUnsubscribe(uuid)) {
			model.addAttribute("listSubscriberUuid", uuid);
			return LIST_SUBSCRIBER_UNSUBSCRIBE_VIEW_NAME;
		} else {
			return UNSUBSCRIBE_ERROR_VIEW_NAME;
		}
	}
}
