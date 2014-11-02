package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRow;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRows;

@Service
public class MemberAPIService {

	@Autowired 
	private MemberService memberService;

	public MemberRows getMemberRows(String memberStatus) {
		List<MemberRow> memberRows = new ArrayList<MemberRow>();
		if ("ALL".equals(memberStatus)) {
			memberRows = memberService.getAll();
		} else if ("CERTIFIABLE".equals(memberStatus)) {
			memberRows = memberService.getAllAwaitingCertificate();
		} else if ("OVERDUE".equals(memberStatus)) {
			memberRows = memberService.getAllOverdue();
		} else if ("UNPAID".equals(memberStatus)) {
			memberRows = memberService.getUnpaidMembers();
		} else if ("PART".equals(memberStatus)) {
			memberRows = memberService.getPartPaidMembers();
		} else {
			memberRows = memberService.getByMemberStatus(memberStatus);
		}
		
		return new MemberRows(memberRows);
	}
}
