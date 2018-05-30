package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.ContactListDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.StockEmailDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.StockEmailRequestDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.email.EmailAttachment;
import uk.co.village_greens_coop.VillageGreensMemberPortal.email.EmailDetail;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.SendStockEmailForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.StockEmailForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.*;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.DocumentRow;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.StockEmailRow;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class EmailService {

	@Value("${email.unsubscribe.url.base}")
	private String emailUnsubscribeUrlBase;
	@Value("${email.consent.url.base}")
	private String emailConsentUrlBase;

	private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private MemberDao memberRepository;

	@Autowired
	private ContactListDao contactListDao;

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
			LOG.info("Email NOT sent successfully");
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
		stockEmail.setEmailHtmlBody(sef.getEmailHtmlBody());
		
		if (sef.getSelectedDocuments() != null && sef.getSelectedDocuments().size() > 0) {
			for (DocumentRow docRow: sef.getSelectedDocuments()) {
				Document document = documentService.getById(docRow.getId());
				stockEmail.getAttachments().add(document);
			}
		}
		stockEmailRepository.save(stockEmail);
		return stockEmail;
	}

	// given a set of DocumentForm objects (from a submitted form), return true if the given
	// document matches one of the DocumentForms in the set (and removes it from the set)
	private boolean checkDocumentSelectedAndRemove(Document document, List<DocumentRow> selectedDocuments) {
		if (selectedDocuments != null) {
			for (Iterator<DocumentRow> iterator = selectedDocuments.iterator(); iterator.hasNext();) {
				DocumentRow docRow = iterator.next();
				if (document.getId().equals(docRow.getId())) {
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
			for (DocumentRow docRow: sef.getSelectedDocuments()) {
				Document document = documentService.getById(docRow.getId());
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
			if (sendStockEmailForm.getFullConsentedMembers()) {
				List<Member> membersList = memberRepository.getFullConsentedMembers();
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
		
		// process any ad-hoc recipients
		if (sendStockEmailForm.getAdhocRecipients()) {
			List<String> recipientList = getRecipientEmails(sendStockEmailForm);
			for (String recipient : recipientList) {
				StockEmailRequest emailRequest = new StockEmailRequest(stockEmail, recipient);
				stockEmailRequestRepository.save(emailRequest);
				numRequested++;
				LOG.info("Requested stock email {} for adhoc recipient {}", stockEmail.getId(), recipient);
			}
		}

		if (sendStockEmailForm.getMailingList()) {
			ContactList contactList = contactListDao.findConsentedById(Long.parseLong(sendStockEmailForm.getContactListId()));
			if (contactList != null && contactList.getListSubscribers() != null) {
				for (ListSubscriber listSubscriber: contactList.getListSubscribers()) {
					StockEmailRequest emailRequest = new StockEmailRequest(listSubscriber, stockEmail);
					stockEmailRequestRepository.save(emailRequest);
					numRequested++;
					LOG.info("Requested stock email {} for mailing list {} subscriber {}", stockEmail.getId(), contactList.getId(), listSubscriber.getId());
				}
			}
		}

		return numRequested;
	}

	private List<String> getRecipientEmails(SendStockEmailForm form) {
		List<String> list = new ArrayList<String>(); 
		String recipients = form.getRecipients();
		if (recipients != null && !recipients.trim().equals("")) {
			String[] recipientArray = recipients.split(",");
			if (recipientArray != null && recipientArray.length > 0) {
				Collections.addAll(list,  recipientArray);
			}
		}
		return list;
	}
		
	private String populatePlaceholders(String bodyText, Member member, ListSubscriber listSubscriber) {
		String retval = bodyText;
		if (retval != null && member != null) {
			retval = retval.replaceAll("\\$\\{informalSalutation\\}", member.getSalutation(false));
			retval = retval.replaceAll("\\$\\{untitledInformalSalutation\\}", member.getUntitledSalutation(false));
			retval = retval.replaceAll("\\$\\{formalSalutation\\}", member.getSalutation(true));
			retval = retval.replaceAll("\\$\\{untitledformalSalutation\\}", member.getUntitledSalutation(true));
			retval = retval.replaceAll("\\$\\{salutation\\}", member.getSalutation(false));
			retval = retval.replaceAll("\\$\\{holding\\}", new DecimalFormat("###,###").format(member.getTotalInvestment()));
			retval = retval.replaceAll("\\$\\{numshares\\}", "£" + new DecimalFormat("###,###").format(member.getTotalInvestment()));
			retval = retval.replaceAll("\\$\\{memberno\\}", (member.getMemberno() != null ? member.getMemberno().toString() : "n/a"));
			// every member email will now have an "unsubscribe" footer
			if (retval != null && !"".equals(retval.trim())) {
				retval = retval + getUnsubscribeFooter(member);
			}
			retval = retval.replaceAll("\\$\\{marketing_consent_url\\}", emailConsentUrlBase + "m/" + member.getUuid());
			retval = retval.replaceAll("\\$\\{unsubscribe_url\\}", emailUnsubscribeUrlBase + "m/" + member.getUuid());
		} else if (retval != null && listSubscriber != null) {
			retval = retval.replaceAll("\\$\\{marketing_consent_url\\}", emailConsentUrlBase + "l/" + listSubscriber.getUuid());
			retval = retval.replaceAll("\\$\\{unsubscribe_url\\}", emailUnsubscribeUrlBase + "l/" + listSubscriber.getUuid());
			// every list subscriber email will now have an "unsubscribe" footer
			if (retval != null && !"".equals(retval.trim())) {
				retval = retval + getUnsubscribeFooter(listSubscriber);
			}
		}
		return retval;
	}

	private String getUnsubscribeFooter(Member member) {
		return getUnsubscribeFooter(emailUnsubscribeUrlBase + "m/" + member.getUuid());
	}

	private String getUnsubscribeFooter(ListSubscriber listSubscriber) {
		return getUnsubscribeFooter(emailUnsubscribeUrlBase + "l/" + listSubscriber.getUuid());
	}

	private String getUnsubscribeFooter(String url) {
		return String.format("\n\nYou are receiving this email because you subscribed to Village Greens' mailing list. " +
				"If you wish to unsubscribe, click here: %s\n\n" +
				"Village Greens, 1 Longfield Centre, Prestwich, Manchester M25 1AY. UK\n", url);
	}

	@Transactional
	public int sendQueuedStockEmails() {
		int numSent = 0;
		List<StockEmailRequest> emailRequests = stockEmailRequestRepository.findUnsentBatch(10);
		for (StockEmailRequest emailRequest: emailRequests) {
			StockEmail stockEmail = emailRequest.getStockEmail();
			Member member = emailRequest.getMember();
			ListSubscriber listSubscriber = emailRequest.getListSubscriber();
			if (member != null && (member.getEmail() == null || member.getEmail().trim().equals(""))) {
				LOG.warn("Member [id: {}] has no email address - not sending stock email", member.getId());
				emailRequest.setError("No email address found for member");
			} else if (listSubscriber != null && (listSubscriber.getEmail() == null || listSubscriber.getEmail().trim().equals(""))) {
				LOG.warn("ListSubscriber [id: {}] has no email address - not sending stock email", member.getId());
				emailRequest.setError("No email address found for list subscriber");
			} else {
				EmailDetail emailDetail = new EmailDetail();
				if (member == null && listSubscriber == null) {
					LOG.info("Sending stock email [id: {}] to adhoc recipient {}", stockEmail.getId(), emailRequest.getRecipientEmail());
					emailDetail.setFromAddress("customers@village-greens-coop.co.uk");
					emailDetail.setFromDisplay("Village Greens Customers");
					emailDetail.setToAddress(emailRequest.getRecipientEmail());
				} else if (member != null) {
					LOG.info("Sending stock email [id: {}] to member [id: {}]", stockEmail.getId(), member.getId());
					emailDetail.setFromAddress("members@village-greens-coop.co.uk");
					emailDetail.setFromDisplay("Village Greens Members");
					emailDetail.setToAddress(member.getEmail());
				} else if (listSubscriber != null) {
					LOG.info("Sending stock email [id: {}] to list subscriber [id: {}]", stockEmail.getId(), listSubscriber.getId());
					emailDetail.setToAddress(listSubscriber.getEmail());
				}
				emailDetail.setSubject(stockEmail.getEmailSubject());
				emailDetail.setTemplate(populatePlaceholders(stockEmail.getEmailBody(), member, listSubscriber));
				emailDetail.setHtml(populatePlaceholders(stockEmail.getEmailHtmlBody(), member, listSubscriber));
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
		List<DocumentRow> availableDocuments = documentService.getAllDocumentsAsDocumentRows();
		StockEmailForm sef = new StockEmailForm(availableDocuments);
		return sef;
	}

	@SuppressWarnings("unchecked")
	public StockEmailForm getStockEmailForm(StockEmail stockEmail) {
		List<Document> allDocuments = documentService.getAllDocuments();
		List<DocumentRow> availableDocuments = new ArrayList<DocumentRow>();
		List<DocumentRow> selectedDocuments = new ArrayList<DocumentRow>();
		// check each existing attachment, and use them to create the available and selected lists of DocumentForms
		for (Document document: allDocuments) {
			boolean documentSelected = false;
			for (Document attachment: stockEmail.getAttachments()) {
				if (document.getId().equals(attachment.getId())) {
					documentSelected = true;
					selectedDocuments.add(new DocumentRow(document));
					break;
				} 
			}
			if (!documentSelected) {
				availableDocuments.add(new DocumentRow(document)); 
			}
		}
		
		Comparator<DocumentRow> reverseCreationDateComparator = new Comparator<DocumentRow> () {
			@Override
			public int compare(DocumentRow o1, DocumentRow o2) {
				return o2.getCreationTimestampMillis().compareTo(o1.getCreationTimestampMillis());
			}
		};
		
		Collections.sort(availableDocuments, reverseCreationDateComparator);
		Collections.sort(selectedDocuments, reverseCreationDateComparator);

		StockEmailForm sef = new StockEmailForm(stockEmail, availableDocuments, selectedDocuments);
		return sef;
	}
}
