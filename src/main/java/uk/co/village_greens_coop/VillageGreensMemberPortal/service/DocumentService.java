package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.DocumentDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.DocumentForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Document;

@Service
public class DocumentService {

	private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

	@Autowired
	private DocumentDao documentRepository;
	
	@Transactional(readOnly = true)
	public Document getById(Long id) {
		return documentRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public List<Document> getAllDocuments() {
		List<Document> documents = documentRepository.getAll();
		return documents;
	}

	@Transactional(readOnly = true)
	public List<DocumentForm> getAllDocumentsAsForms() {
		List<DocumentForm> docForms = new ArrayList<DocumentForm>();
		List<Document> documents = getAllDocuments();
		for (Document document: documents) {
			docForms.add(new DocumentForm(document));
		}
		return docForms;
	}
	
	@Transactional
	public Document createDocument(DocumentForm df) {
		Document document = new Document();
		document.setFilename(df.getFilename());
		document.setDescription(df.getDescription());
		document.setCreationTimestamp(new Date());
		documentRepository.save(document);
		return document;
	}
	
	@Transactional
	public Document createDocument(String filename, String description) {
		Document document = new Document();
		document.setFilename(filename);
		if (description == null || description.trim().equals("")) {
			description = filename;
		}
		document.setDescription(description);
		document.setCreationTimestamp(new Date());
		documentRepository.save(document);
		return document;
	}

	@Transactional
	public Document updateDocument(DocumentForm df) {
		Document document = documentRepository.findById(df.getId());
		document.setFilename(df.getFilename());
		document.setDescription(df.getDescription());
		documentRepository.save(document);
		return document;
	}
	
	
	@Transactional
	public void deleteDocument(Long id) {
		Document document = documentRepository.findById(id);
		documentRepository.delete(document);
	}

}
