package uk.co.village_greens_coop.VillageGreensMemberPortal.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.co.village_greens_coop.VillageGreensMemberPortal.form.MemberForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRows;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.MemberAPIService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.MemberService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberAPIService memberAPIService;
	
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
    	return "admin/members";
    }
    
    @RequestMapping(value = "partialMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getPartialMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "PART");
    	return "admin/members";
    }
    
    @RequestMapping(value = "unpaidMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getUnpaidMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "UNPAID");
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

    	model.addAttribute("memberStatus", memberStatus);
    	Member member = memberService.getById(id);
    	if (member != null) {
    		MemberForm mf = new MemberForm(member);
        	model.addAttribute("memberForm", mf);
        	return "admin/member";
    	} else {
    		//TODO add an error message, the id wasn't found
    		return "admin/members";
    	}
    }
    
}
