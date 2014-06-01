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

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.PasswordReset;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.SignUpService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.signup.ForgottenPasswordForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.signup.PasswordResetForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.signup.SignupForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.support.web.MessageHelper;

@Controller
public class PasswordResetController {

	private static final Logger LOG = LoggerFactory.getLogger(PasswordResetController.class);
	private static final String FORGOTTEN_PASSWORD_VIEW_NAME = "resetPassword/forgottenPassword";
	private static final String RESET_PASSWORD_VIEW_NAME = "resetPassword/resetPassword";

	@Autowired
	private SignUpService signUpService;
	
	@RequestMapping(value = "forgottenPassword")
	public String getForgottenPasswordForm(Model model) {
		LOG.info("New forgotten password request detected");
		model.addAttribute(new ForgottenPasswordForm());
        return FORGOTTEN_PASSWORD_VIEW_NAME;
	}

	@RequestMapping(value = "forgottenPassword", method = RequestMethod.POST)
	public String requestPasswordReset(HttpServletRequest request,
						@Valid @ModelAttribute ForgottenPasswordForm forgottenPasswordForm, 
						BindingResult result, 
						Errors errors, 
						RedirectAttributes ra) {
		if (errors.hasErrors()) {
			LOG.info("Errors in forgotten password form");
			return FORGOTTEN_PASSWORD_VIEW_NAME;
		}
		
		signUpService.forgottenPassword(request, forgottenPasswordForm.getEmail());

		MessageHelper.addInfoAttribute(ra, "forgotten.password.request.success");
		return "redirect:/signin";
	}
	
	@RequestMapping(value = "resetPassword", method = RequestMethod.GET)
	public String activate(@RequestParam Long id,
							@RequestParam String key,
							Model model,
							RedirectAttributes ra) {
		LOG.info("New password reset request for id {} with key {}", id, key);
		PasswordResetForm passwordResetForm = signUpService.getAccountDetailsForPasswordReset(id, key);
		if (passwordResetForm != null) {
			model.addAttribute(passwordResetForm);
	        return RESET_PASSWORD_VIEW_NAME;
		} 
		
		MessageHelper.addWarningAttribute(ra, "password.reset.expired");
		return "redirect:/signin";
	}
	
	@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
	public String requestPasswordReset(HttpServletRequest request,
						@Valid @ModelAttribute PasswordResetForm passwordResetForm, 
						BindingResult result, 
						Errors errors, 
						RedirectAttributes ra) {
		convertPasswordError(result);
		if (errors.hasErrors()) {
			LOG.info("Errors in forgotten password form");
			return RESET_PASSWORD_VIEW_NAME;
		}
		
		boolean passwordUpdated = signUpService.updateViaPasswordReset(request, passwordResetForm.getId(), passwordResetForm.getKey(), passwordResetForm.getPassword());

		if (passwordUpdated) {
			MessageHelper.addInfoAttribute(ra, "password.reset.success");
			return "redirect:/signin";
		} else {
			MessageHelper.addWarningAttribute(ra, "password.reset.failure");
			return "redirect:/signin";
		}
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

}
