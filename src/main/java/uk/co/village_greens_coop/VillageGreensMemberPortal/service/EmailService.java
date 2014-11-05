package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.SendStockEmailForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.StockEmailForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmail;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmailRequest;

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
			emailForms.add(new StockEmailForm(stockEmail));
		}
		return emailForms;
	}
	
	@Async
	public void sendEmail(EmailDetail emailDetail) {
		LOG.info("Sending email ", emailDetail.toString());
		try {
			final JavaMailSender javaMailSender = (JavaMailSender)mailSender;
			final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
			message.setFrom(emailDetail.getFromAddress(), emailDetail.getFromDisplay());
			message.setTo(emailDetail.getToAddress());
			message.setSubject(emailDetail.getSubject());
			message.setText(emailDetail.getTemplate());
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
		stockEmailRepository.save(stockEmail);
		return stockEmail;
	}
	
	@Transactional
	public StockEmail updateStockEmail(StockEmailForm sef) {
		StockEmail stockEmail = stockEmailRepository.findById(sef.getId());
		stockEmail.setEmailPurpose(sef.getEmailPurpose());
		stockEmail.setEmailSubject(sef.getEmailSubject());
		stockEmail.setEmailBody(sef.getEmailBody());
		stockEmailRepository.save(stockEmail);
		return stockEmail;
	}
	
	@Transactional
	public void deleteStockEmail(Long id) {
		StockEmail stockEmail = stockEmailRepository.findById(id);
		stockEmailRepository.delete(stockEmail);
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
				List<Member> membersList = memberRepository.getCommitteeMembers();
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
		retval = retval.replaceAll("\\$\\{informalSalutation\\}", member.getSalutation(false));
		retval = retval.replaceAll("\\$\\{untitledInformalSalutation\\}", member.getUntitledSalutation(false));
		retval = retval.replaceAll("\\$\\{formalSalutation\\}", member.getSalutation(true));
		retval = retval.replaceAll("\\$\\{untitledformalSalutation\\}", member.getUntitledSalutation(true));
		retval = retval.replaceAll("\\$\\{salutation\\}", member.getSalutation(false));
		retval = retval.replaceAll("\\$\\{holding\\}", new DecimalFormat("###,###").format(member.getTotalInvestment()));
		retval = retval.replaceAll("\\$\\{numshares\\}", "£" + new DecimalFormat("###,###").format(member.getTotalInvestment()));
		retval = retval.replaceAll("\\$\\{memberno\\}", (member.getMemberno() != null ? member.getMemberno().toString() : "n/a"));
		return retval;
	}
	
	@Transactional
	public int sendQueuedStockEmails() {
		int numSent = 0;
		List<StockEmailRequest> emailRequests = stockEmailRequestRepository.findUnsentBatch(10);
		for (StockEmailRequest emailRequest: emailRequests) {
			StockEmail stockEmail = emailRequest.getStockEmail();
			Member member = emailRequest.getMember();
			EmailDetail emailDetail = new EmailDetail();
			emailDetail.setFromAddress("info@village-greens-coop.co.uk");
			emailDetail.setFromDisplay("Village Greens Info");
			emailDetail.setSubject(stockEmail.getEmailSubject());
			emailDetail.setTemplate(populateMemberPlaceholders(stockEmail.getEmailBody(), member));
			emailDetail.setToAddress(member.getEmail());
			sendEmail(emailDetail);
			if (emailDetail.getError() != null) {
				emailRequest.setError(emailDetail.getError());
			} else {
				emailRequest.setSentTimestamp(new Date());
			}
			stockEmailRepository.save(stockEmail);
			numSent++;
		}
		return numSent;
	}
}
