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
		MessageHelper.addErrorAttribute(ra, "signin.badcredentials");
		return "redirect:/signin";
	}
	
	@RequestMapping(value = "activate", method = RequestMethod.GET)
	public String accessDeniedActivation(Model model, RedirectAttributes ra) {
		MessageHelper.addErrorAttribute(ra, "signin.activate");
		return "redirect:/signin";
	}

	@RequestMapping(value = "error", method = RequestMethod.GET)
	public String accessDeniedError(Model model, RedirectAttributes ra) {
		MessageHelper.addErrorAttribute(ra, "signin.error");
		return "redirect:/signin";
	}
}
