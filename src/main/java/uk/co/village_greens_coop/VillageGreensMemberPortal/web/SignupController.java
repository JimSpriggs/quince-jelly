package uk.co.village_greens_coop.VillageGreensMemberPortal.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.AccountDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.SignUpService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.UserService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.signup.SignupForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.support.web.MessageHelper;

@Controller
public class SignupController {

	private static final Logger LOG = LoggerFactory.getLogger(SignupController.class);
	private static final String SIGNUP_VIEW_NAME = "signup/signup";

	@Autowired
	private AccountDao accountRepository;
	
	@Autowired
	private SignUpService signUpService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "signup")
	public String signup(Model model) {
		LOG.info("New signup detected");
		model.addAttribute(new SignupForm());
        return SIGNUP_VIEW_NAME;
	}
	
	@RequestMapping(value = "signup", method = RequestMethod.POST)
	public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
		if (errors.hasErrors()) {
			LOG.info("Errors in signup form");
			return SIGNUP_VIEW_NAME;
		}
		
		signUpService.signup(signupForm.getEmail(), signupForm.getPassword());
        // see /WEB-INF/i18n/messages.properties and /WEB-INF/views/homeSignedIn.html
        MessageHelper.addSuccessAttribute(ra, "signup.success");
		return "redirect:/";
	}
}
