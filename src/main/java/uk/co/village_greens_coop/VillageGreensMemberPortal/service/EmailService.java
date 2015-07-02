package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.StockEmailDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.StockEmailRequestDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.email.EmailAttachment;
import uk.co.village_greens_coop.VillageGreensMemberPortal.email.EmailDetail;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.DocumentForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.SendStockEmailForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.StockEmailForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Document;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmail;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmailRequest;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.StockEmailRow;

@Service
public class EmailService {

	private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private MemberDao memberRepository;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private StockEmailDao stockEmailRepository;
	
	@Autowired
	private StockEmailRequestDao stockEmailRequestRepository;
	
	@Autowired
	private DocumentService documentService;
	
	@Transactional(readOnly = true)
	public EmailDetail getStockEmailDetail(String purpose) {
		StockEmail email = stockEmailRepository.findByPurpose(purpose);
		if (email != null) {
			EmailDetail emailDetail = new EmailDetail();
			emailDetail.setSubject(email.getEmailSubject());
			emailDetail.setTemplate(email.getEmailBody());
			return emailDetail;
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public StockEmail getStockEmailById(Long id) {
		return stockEmailRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public List<StockEmailForm> getAllEmails() {
		List<StockEmailForm> emailForms = new ArrayList<StockEmailForm>();
		List<StockEmail> emails = stockEmailRepository.getAll();
		for (StockEmail stockEmail: emails) {
			emailForms.add(new StockEmailForm(stockEmail, null, null));
		}
		return emailForms;
	}

	@Transactional(readOnly = true)
	public List<StockEmailRow> getAllStockEmailRows() {
		List<StockEmail> emails = stockEmailRepository.getAll();
		List<StockEmailRow> rows = new ArrayList<StockEmailRow>();
		for (StockEmail email: emails) {
			StockEmailRow row = new StockEmailRow(email);
			rows.add(row);
		}
		return rows;
	}
	@Async
	public void sendEmail(EmailDetail emailDetail) {
		LOG.info("Sending email " + emailDetail.toString());
		try {
			final JavaMailSender javaMailSender = (JavaMailSender)mailSender;
			final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
			message.setFrom(emailDetail.getFromAddress(), emailDetail.getFromDisplay());
			message.setTo(emailDetail.getToAddress());
			message.setSubject(emailDetail.getSubject());
			
			if (emailDetail.getHtml() != null &&
					!emailDetail.getHtml().equals("")) {
				MimeMultipart mp = new MimeMultipart("alternative");
				mimeMessage.setContent(mp);
				// create the plain text mime part
				BodyPart plainText = new MimeBodyPart();
				plainText.setText(emailDetail.getTemplate());
				mp.addBodyPart(plainText);
				// create the html mime part
				BodyPart html = new MimeBodyPart();
				html.setContent(emailDetail.getHtml(), "text/html");
				mp.addBodyPart(html);
			} else {
				// just plain text, set it on the helper
				message.setText(emailDetail.getTemplate());
			}
			
			if (emailDetail.hasAttachments()) {
				for (EmailAttachment attachment : emailDetail.getAttachments()) {
					message.addAttachment(attachment.getAttachmentFileName(), new File(attachment.getFullPathAndFileName()));
				}
			}
			
			javaMailSender.send(mimeMessage);
			LOG.info("Email sent successfully");
		} catch (MessagingException e) {
			LOG.error("Unable to send message: {}", emailDetail.toString(), e);
			emailDetail.setError(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			LOG.error("Unable to send message: {}", emailDetail.toString(), e);
			emailDetail.setError(e.getMessage());
		}
	}
	
	@Transactional
	public StockEmail createStockEmail(StockEmailForm sef) {
		StockEmail stockEmail = new StockEmail();
		stockEmail.setEmailPurpose(sef.getEmailPurpose());
		stockEmail.setEmailSubject(sef.getEmailSubject());
		stockEmail.setEmailBody(sef.getEmailBody());
		
		if (sef.getSelectedDocuments() != null && sef.getSelectedDocuments().size() > 0) {
			for (DocumentForm docForm: sef.getSelectedDocuments()) {
				Document document = documentService.getById(docForm.getId());
				stockEmail.getAttachments().add(document);
			}
		}
		stockEmailRepository.save(stockEmail);
		return stockEmail;
	}

	// given a set of DocumentForm objects (from a submitted form), return true if the given
	// document matches one of the DocumentForms in the set (and removes it from the set)
	private boolean checkDocumentSelectedAndRemove(Document document, List<DocumentForm> selectedDocuments) {
		if (selectedDocuments != null) {
			for (Iterator<DocumentForm> iterator = selectedDocuments.iterator(); iterator.hasNext();) {
				DocumentForm docForm = iterator.next();
				if (document.getId().equals(docForm.getId())) {
					iterator.remove();
					return true;
				}
			}
		}
		return false;
	}
	
	@Transactional
	public StockEmail updateStockEmail(StockEmailForm sef) {
		StockEmail stockEmail = stockEmailRepository.findById(sef.getId());
		stockEmail.setEmailPurpose(sef.getEmailPurpose());
		stockEmail.setEmailSubject(sef.getEmailSubject());
		stockEmail.setEmailBody(sef.getEmailBody());
		stockEmail.setEmailHtmlBody(sef.getEmailHtmlBody());
		
		// first work out which current documents need to be removed
		for (Iterator<Document> iterator = stockEmail.getAttachments().iterator(); iterator.hasNext();) {
			Document document = iterator.next();
			if (!checkDocumentSelectedAndRemove(document, sef.getSelectedDocuments())) {
				iterator.remove();
			}
		}
		
		// now the selected documents list contains only those documents which need to be added
		if (sef.getSelectedDocuments() != null && sef.getSelectedDocuments().size() > 0) {
			for (DocumentForm docForm: sef.getSelectedDocuments()) {
				Document document = documentService.getById(docForm.getId());
				stockEmail.getAttachments().add(document);
			}
		}

		stockEmailRepository.save(stockEmail);
		return stockEmail;
	}
	
	@Transactional
	public String deleteStockEmail(Long id) {
		StockEmail stockEmail = stockEmailRepository.findById(id);
		stockEmailRepository.delete(stockEmail);
		return stockEmail.getEmailPurpose();
	}

	@Transactional
	public int sendStockEmail(SendStockEmailForm sendStockEmailForm) {
		StockEmail stockEmail = stockEmailRepository.findById(sendStockEmailForm.getEmailId());
		if (stockEmail == null) { 
			LOG.error("No stock email found to send with id {}", sendStockEmailForm.getEmailId());
			return -1; 
		}
		
		// use a set to deduplicate, as some of the sets of members could intersect
		// for example part-paid and overdue could contain the same member
		Set<Member> membersToSend = new HashSet<Member>();
		if (sendStockEmailForm.getAllMembers()) {
			List<Member> membersList = memberRepository.getAll();
			membersToSend.addAll(membersList);
		} else {
			if (sendStockEmailForm.getFullMembers()) {
				List<Member> membersList = memberRepository.getFullMembers();
				membersToSend.addAll(membersList);
			}
			if (sendStockEmailForm.getPartMembers()) {
				List<Member> membersList = memberRepository.getPartPaidMembers();
				membersToSend.addAll(membersList);
			}
			if (sendStockEmailForm.getUnpaidMembers()) {
				List<Member> membersList = memberRepository.getUnpaidMembers();
				membersToSend.addAll(membersList);
			}
			if (sendStockEmailForm.getOverdueMembers()) {
				List<Member> membersList = memberRepository.getOverdueMembers();
				membersToSend.addAll(membersList);
			}
			if (sendStockEmailForm.getCommitteeMembers()) {
				List<Member> membersList = memberRepository.getCommitteeAndSysAdminMembers();
				membersToSend.addAll(membersList);
			}
		}
		
		// now for each member identified, create and persist a StockEmailRequest
		int numRequested = 0;
		for (Member member: membersToSend) {
			StockEmailRequest emailRequest = new StockEmailRequest(member, stockEmail);
			stockEmailRequestRepository.save(emailRequest);
			numRequested++;
			LOG.info("Requested stock email {} for member {}", stockEmail.getId(), member.getId());
		}
		
		return numRequested;
	}
	
	private String populateMemberPlaceholders(String bodyText, Member member) {
		String retval = bodyText;
		if (retval != null) {
			retval = retval.replaceAll("\\$\\{informalSalutation\\}", member.getSalutation(false));
			retval = retval.replaceAll("\\$\\{untitledInformalSalutation\\}", member.getUntitledSalutation(false));
			retval = retval.replaceAll("\\$\\{formalSalutation\\}", member.getSalutation(true));
			retval = retval.replaceAll("\\$\\{untitledformalSalutation\\}", member.getUntitledSalutation(true));
			retval = retval.replaceAll("\\$\\{salutation\\}", member.getSalutation(false));
			retval = retval.replaceAll("\\$\\{holding\\}", new DecimalFormat("###,###").format(member.getTotalInvestment()));
			retval = retval.replaceAll("\\$\\{numshares\\}", "£" + new DecimalFormat("###,###").format(member.getTotalInvestment()));
			retval = retval.replaceAll("\\$\\{memberno\\}", (member.getMemberno() != null ? member.getMemberno().toString() : "n/a"));
		}
		return retval;
	}
	
	@Transactional
	public int sendQueuedStockEmails() {
		int numSent = 0;
		List<StockEmailRequest> emailRequests = stockEmailRequestRepository.findUnsentBatch(10);
		for (StockEmailRequest emailRequest: emailRequests) {
			StockEmail stockEmail = emailRequest.getStockEmail();
			Member member = emailRequest.getMember();
			if (member.getEmail() == null || member.getEmail().trim().equals("")) {
				LOG.warn("Member [id: {}] has no email address - not sending stock email", member.getId());
				emailRequest.setError("No email address found for member");
			} else {
				LOG.info("Sending stock email [id: {}] to member [id: {}]", stockEmail.getId(), member.getId());
				EmailDetail emailDetail = new EmailDetail();
				emailDetail.setFromAddress("members@village-greens-coop.co.uk");
				emailDetail.setFromDisplay("Village Greens Members");
				emailDetail.setSubject(stockEmail.getEmailSubject());
				emailDetail.setTemplate(populateMemberPlaceholders(stockEmail.getEmailBody(), member));
				emailDetail.setHtml(populateMemberPlaceholders(stockEmail.getEmailHtmlBody(), member));
				emailDetail.setToAddress(member.getEmail());
				if (stockEmail.getAttachments() != null && stockEmail.getAttachments().size() > 0) {
					EmailAttachment[] attachments = new EmailAttachment[stockEmail.getAttachments().size()];
					int index = 0;
					for (Document document: stockEmail.getAttachments()) {
						EmailAttachment attachment = new EmailAttachment(document);
						attachments[index] = attachment;
						index++;
					}
					emailDetail.setAttachments(attachments);
				}
				sendEmail(emailDetail);
				if (emailDetail.getError() != null) {
					emailRequest.setError(emailDetail.getError());
				} else {
					emailRequest.setSentTimestamp(new Date());
				}
				numSent++;
			}
			stockEmailRepository.save(stockEmail);
		}
		return numSent;
	}

	public StockEmailForm getStockEmailForm() {
		List<DocumentForm> availableDocuments = documentService.getAllDocumentsAsForms();
		StockEmailForm sef = new StockEmailForm(availableDocuments);
		return sef;
	}

	public StockEmailForm getStockEmailForm(StockEmail stockEmail) {
		List<Document> allDocuments = documentService.getAllDocuments();
		List<DocumentForm> availableDocuments = new ArrayList<DocumentForm>();
		List<DocumentForm> selectedDocuments = new ArrayList<DocumentForm>();
		// check each existing attachment, and use them to create the available and selected lists of DocumentForms
		for (Document document: allDocuments) {
			boolean documentSelected = false;
			for (Document attachment: stockEmail.getAttachments()) {
				if (document.getId().equals(attachment.getId())) {
					documentSelected = true;
					selectedDocuments.add(new DocumentForm(document));
					break;
				} 
			}
			if (!documentSelected) {
				availableDocuments.add(new DocumentForm(document)); 
			}
		}
		StockEmailForm sef = new StockEmailForm(stockEmail, availableDocuments, selectedDocuments);
		return sef;
	}
}
