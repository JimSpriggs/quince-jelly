package uk.co.village_greens_coop.VillageGreensMemberPortal.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value = "/members", method = RequestMethod.GET)
	public String list(Model model) {
		List<Member> list = memberService.getAll();
		model.addAttribute("members", list);
		return "members/members";
	}
}