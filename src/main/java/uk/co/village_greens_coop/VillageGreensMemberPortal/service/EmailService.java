package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import uk.co.village_greens_coop.VillageGreensMemberPortal.email.EmailAttachment;
import uk.co.village_greens_coop.VillageGreensMemberPortal.email.EmailDetail;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;

@Service
public class EmailService {

	private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private MailSender mailSender;
	
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
		} catch (UnsupportedEncodingException e) {
			LOG.error("Unable to send message: {}", emailDetail.toString(), e);
		}
	}
}
