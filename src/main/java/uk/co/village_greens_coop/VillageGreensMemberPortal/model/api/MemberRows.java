package uk.co.village_greens_coop.VillageGreensMemberPortal.model.api;

import java.util.List;

public class MemberRows {
	
	private List<MemberRow> data;

	public MemberRows(List<MemberRow> memberRows) {
		this.data = memberRows;
	}
	
	public List<MemberRow> getMemberRows() {
		return data;
	}

	public void setMemberRows(List<MemberRow> memberRows) {
		this.data = memberRows;
	}
	
}
