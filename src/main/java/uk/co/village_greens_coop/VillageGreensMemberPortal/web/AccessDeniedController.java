package uk.co.village_greens_coop.VillageGreensMemberPortal.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.co.village_greens_coop.VillageGreensMemberPortal.support.web.MessageHelper;

@Controller
@RequestMapping(value = "/accessDenied")
public class AccessDeniedController {
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {
		if (principal != null) {
			model.addAttribute("errorMessage", "Sorry - you're not allowed to access that page.");
			return "error/accessDenied";
		} else {
			return "signin/signin";
		}
	}

	@RequestMapping(value = "credentials", method = RequestMethod.GET)
	public String accessDeniedCredentials(RedirectAttributes ra) {
//		model.addAttribute("errorMessage", "Sorry - the username and password are not recognised.");
//		MessageHelper.addSuccessAttribute(ra, "signup.success");
		MessageHelper.addErrorAttribute(ra, "signin.badcredentials");
		return "redirect:/signin";
	}
	
	@RequestMapping(value = "activate", method = RequestMethod.GET)
	public String accessDeniedActivation(Model model, RedirectAttributes ra) {
//		model.addAttribute("errorMessage", "Sorry - you haven't yet activated your account using the email we sent.");
//		MessageHelper.addErrorAttribute(model, "Sorry - you haven't yet activated your account using the email we sent.");
		MessageHelper.addErrorAttribute(ra, "signin.activate");
		return "redirect:/signin";
	}
}
