package uk.co.village_greens_coop.VillageGreensMemberPortal.model.api;

import java.util.List;

public class DocumentRows {
	
	private List<DocumentRow> data;

	public DocumentRows(List<DocumentRow> documentRows) {
		this.data = documentRows;
	}
	
	public List<DocumentRow> getDocumentRows() {
		return data;
	}

	public void setDocumentRows(List<DocumentRow> documentRows) {
		this.data = documentRows;
	}
	
}
