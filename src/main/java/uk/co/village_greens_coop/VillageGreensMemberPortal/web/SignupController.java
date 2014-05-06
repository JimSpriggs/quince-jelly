package uk.co.village_greens_coop.VillageGreensMemberPortal.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	private static final String SIGNIN_VIEW_NAME = "signin/signin";

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
	
	@RequestMapping(value = "activate")
	public String activate(@RequestParam Long id,
							@RequestParam String key,
							RedirectAttributes ra) {
		LOG.info("New activation detected for id {} with key {}", id, key);
		if (signUpService.activate(id, key)) {
	        MessageHelper.addInfoAttribute(ra, "activation.success");
		}
		return "redirect:/signin";
	}
	
	@RequestMapping(value = "signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request,
						@Valid @ModelAttribute SignupForm signupForm, 
						BindingResult result, 
						Errors errors, 
						RedirectAttributes ra) {
		convertPasswordError(result);
		if (!errors.hasErrors()) {
			validateUniqueEmail(signupForm.getEmail(), errors);
		}
		
		if (errors.hasErrors()) {
			LOG.info("Errors in signup form");
			return SIGNUP_VIEW_NAME;
		}
		
		signUpService.signup(request,
					signupForm.getFirstName(),
					signupForm.getSurname(),
					signupForm.getEmail(), 
					signupForm.getPassword());
        // see /WEB-INF/i18n/messages.properties and /WEB-INF/views/homeSignedIn.html
        MessageHelper.addInfoAttribute(ra, "signup.success");
		return "redirect:/signin";
	}
	
	private void convertPasswordError(BindingResult result) {
		for (ObjectError error : result.getGlobalErrors()) {
			String msg = error.getDefaultMessage();
			if (SignupForm.PASSWORD_MISMATCH.equals(msg)) {
				if (!result.hasFieldErrors("repeatPassword")) {
					result.rejectValue("repeatPassword", SignupForm.PASSWORD_MISMATCH);
				}
			}
		}
		
	}
	private void validateUniqueEmail(String email, Errors errors) {
		if (accountRepository.findByEmail(email) != null) {
			errors.rejectValue("email", "error.duplicate.account.email", new String[] { email }, null);
		}
	}
}
