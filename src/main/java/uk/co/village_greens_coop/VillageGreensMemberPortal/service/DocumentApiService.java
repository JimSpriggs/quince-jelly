package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.DocumentRows;

@Service
public class DocumentApiService {

	private static final Logger LOG = LoggerFactory.getLogger(DocumentApiService.class);

	@Autowired
	private DocumentService documentService;
	
	public DocumentRows getAllDocumentRows() {
		return new DocumentRows(documentService.getAllDocumentRows());
	}
}
