package uk.co.village_greens_coop.VillageGreensMemberPortal.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.co.village_greens_coop.VillageGreensMemberPortal.form.MemberForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRows;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.MemberAPIService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.MemberService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	
	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberAPIService memberAPIService;
	
//    @InitBinder("memberForm")
//    private void initBinder(WebDataBinder binder) {
//    	// we want standard validation, plus also a special TelephoneFormValidator
//    	binder.setValidator(new MemberFormValidator(new TelephoneFormValidator()));
//    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getAdminDashboard(HttpServletRequest request) {
    	return "admin/dashboard";
    }

    @RequestMapping(value = "allMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getAllMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "ALL");
    	return "admin/members";
    }
    
    @RequestMapping(value = "fullMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getFullMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "FULL");
    	request.getSession().setAttribute("memberStatus", "FULL");
    	return "admin/members";
    }
    
    @RequestMapping(value = "partialMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getPartialMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "PART");
    	request.getSession().setAttribute("memberStatus", "PART");
    	return "admin/members";
    }
    
    @RequestMapping(value = "unpaidMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getUnpaidMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "UNPAID");
    	request.getSession().setAttribute("memberStatus", "UNPAID");
    	return "admin/members";
    }
    
    @RequestMapping(value = "memberRows", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody MemberRows getMemberRows(
    			@RequestParam(value = "memberStatus") String memberStatus, 
    			HttpServletRequest request) {
    	return memberAPIService.getMemberRows(memberStatus);
    }
    
    @RequestMapping(value = "member", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getMember(
    		@RequestParam(value = "id") Long id,
    		@RequestParam(value = "memberStatus") String memberStatus,
    		HttpServletRequest request, Model model) {

    	Member member = memberService.getById(id);
    	if (member != null) {
    		MemberForm mf = new MemberForm(member);
        	model.addAttribute("memberForm", mf);
        	request.getSession().setAttribute("memberId", id);
        	return "admin/member";
    	} else {
    		//TODO add an error message, the id wasn't found
    		return "redirect:allMembers";
    	}
    }
    
    @RequestMapping(value = "member", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public String saveMember(
    		@Valid @ModelAttribute (value = "memberForm") MemberForm memberForm,
			BindingResult result, 
			Errors errors, 
			RedirectAttributes ra,
			Model model,
			HttpServletRequest request) {
    	
    	// perform deeper validation of the member form (particularly the nested list of TelephoneForms
//    	MemberFormValidator mfv = new MemberFormValidator(new TelephoneFormValidator());
//    	mfv.validate(memberForm, errors);
    	
    	if (errors.hasErrors()) {
    		LOG.info("Errors in Member form");
    		List<FieldError> fieldErrors = errors.getFieldErrors();
    		
    		// look for any telephone field errors
    		for (FieldError fieldError: fieldErrors) {
    			if (fieldError.getField().startsWith("telephones")) {
    	    		model.addAttribute("telephoneErrors", "true");
    			} else if (fieldError.getField().startsWith("payments")) {
    	    		model.addAttribute("paymentErrors", "true");
    			}
    		}
    			
    		return "admin/member";
    	}

    	if (memberForm.getUpdateState().equals("U")) {
        	memberService.updateMemberFromForm(memberForm);
    	} else if (memberForm.getUpdateState().equals("N")) {
    		//TODO handle new members
    	}
    	
    	String memberStatus = (String)request.getSession().getAttribute("memberStatus");
    	String returnString = "allMembers";
    	
    	if (memberStatus != null) {
    		if (memberStatus.equals("FULL")) {
    			returnString = "fullMembers";
    		} else if (memberStatus.equals("PART")) {
    			returnString = "partialMembers";
    		} else if (memberStatus.equals("UNPAID")) {
    			returnString = "unpaidMembers";
    		}
    	}
    	
    	return "redirect:/admin/" + returnString;
    }
}
