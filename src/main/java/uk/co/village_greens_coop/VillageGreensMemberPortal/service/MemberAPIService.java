package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRow;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRows;

@Service
public class MemberAPIService {

	@Autowired 
	private MemberService memberService;
	
	public MemberRows getAllMemberRows() {
		List<MemberRow> memberRows = new ArrayList<MemberRow>();
		List<Member> members = memberService.getAll();
		if (members != null) {
			for (Member member : members) {
				MemberRow memberRow = new MemberRow(member);
				memberRows.add(memberRow);
			}
		}
		
		return new MemberRows(memberRows);
	}
}
