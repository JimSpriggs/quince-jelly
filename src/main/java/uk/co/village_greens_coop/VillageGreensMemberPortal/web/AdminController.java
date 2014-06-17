package uk.co.village_greens_coop.VillageGreensMemberPortal.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRows;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.MemberAPIService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	
	@Autowired
	private MemberAPIService memberAPIService;
	
    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getAdminDashboard(HttpServletRequest request) {
    	return "admin/dashboard";
    }

    @RequestMapping(value = "members", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getAdminMembers(HttpServletRequest request) {
    	return "admin/members";
    }
    
    @RequestMapping(value = "memberRows", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody MemberRows getMemberRows(HttpServletRequest request) {
    	return memberAPIService.getAllMemberRows();
    }
}
