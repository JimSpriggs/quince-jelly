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
import javax.validation.Valid;

@Controller
@RequestMapping("email")
public class EmailConsentController {

	private static final Logger LOG = LoggerFactory.getLogger(EmailConsentController.class);
	private static final String CONSENT_VIEW_NAME = "email/consent";
	private static final String CONSENT_ERROR_VIEW_NAME = "email/consent-error";
	private static final String UNSUBSCRIBE_VIEW_NAME = "email/unsubscribe";
	private static final String UNSUBSCRIBE_ERROR_VIEW_NAME = "email/unsubscribe-error";

	@Autowired
	private ConsentService consentService;

	@RequestMapping(value = "c/{uuid}")
	public String consent(Model model, @PathVariable("uuid") String uuid) {
		LOG.info("New consent request detected");
		if (consentService.captureConsent(uuid)) {
			return CONSENT_VIEW_NAME;
		} else {
			return CONSENT_ERROR_VIEW_NAME;
		}
	}

	@RequestMapping(value = "u/{uuid}")
	public String unsubscribe(Model model, @PathVariable("uuid") String uuid) {
		LOG.info("New unsubscribe request detected");
		if (consentService.unsubscribe(uuid)) {
			model.addAttribute("memberUuid", uuid);
			return UNSUBSCRIBE_VIEW_NAME;
		} else {
			return UNSUBSCRIBE_ERROR_VIEW_NAME;
		}
	}
}
