package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.utils.Utils;
import uk.co.village_greens_coop.VillageGreensMemberPortal.web.SignupController;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class CertificateService {

	private static final Logger LOG = LoggerFactory.getLogger(CertificateService.class);
	
	@Autowired
	private MailSender mailSender;

	@Autowired
	private MemberService memberService;

	public byte[] generateCertificate(Member member) {
		byte[] retval = null;
		
		Document document = new Document(PageSize.A4.rotate());
		Font fcaFont = new Font(FontFamily.HELVETICA, 10);
		Font fieldLabelFont = new Font(FontFamily.HELVETICA, 12);
		Font fieldValueFont = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
		
        try {
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            
            Image image = null;
            try {
                image = Image.getInstance(Utils.getClassPathResourceIntoByteArray("images/Certificate-VG-Logo.png"));
	            document.open();
            } catch (IOException ioe) {
            	LOG.error("IOException getting image and opening new PDF document ", ioe);
            	return retval;
            }

            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
            
            Paragraph paragraph1 = new Paragraph();
            paragraph1.setSpacingAfter(25);
            paragraph1.setAlignment(Element.ALIGN_LEFT);
            paragraph1.setIndentationLeft(25);
            paragraph1.setIndentationRight(25);

            Chunk fcaChunk1 = new Chunk(
            		"Village Greens (Prestwich) Co-operative Ltd is a Co-operative Society "
            		+ "incorporated under the Industrial and Provident Societies Act 1965.",
            		fcaFont);
            Chunk fcaChunk2 = new Chunk(
            		"Registered with the Financial Conduct Authority. Registered No : 32113R",
            		fcaFont);
            paragraph1.add(fcaChunk1);
            paragraph1.add(Chunk.NEWLINE);
            paragraph1.add(fcaChunk2);
            
            Paragraph paragraph2 = new Paragraph();
            paragraph2.setSpacingAfter(25);
            paragraph2.setAlignment(Element.ALIGN_LEFT);
            paragraph2.setIndentationLeft(25);
            paragraph2.setIndentationRight(25);
            
            Chunk shareCertNumberChunk1 = new Chunk("Share Certificate Number: ", fieldLabelFont);
            paragraph2.add(shareCertNumberChunk1);
            Chunk shareCertNumberChunk2 = new Chunk(new DecimalFormat("00000").format(member.getMemberNo()), fieldValueFont);
            paragraph2.add(shareCertNumberChunk2);

            document.add(paragraph1);
            document.add(paragraph2);
            
            document.close(); // no need to close PDFwriter?

            retval = baos.toByteArray();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
        return retval;
	}
	
	@Transactional
	public List<Member> generateMemberCertificates(int limit, String emailTo) {
		List<Member> membersList = memberService.getAllAwaitingCertificate(limit);
		LOG.info("Generating {} certificates...", membersList.size());

		Map<Member, String> membersCerts = new HashMap<Member, String>();
		
		for (Member member : membersList) {
			String certFile = null;
			try {
				certFile = generateMemberCertificate(member);
				memberService.markCertificateGenerated(member);
			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
			// if we generated a cert, and there is an email address for the member, send the cert
			if (certFile != null) { // && member.getEmail() != null && !member.getEmail().equals("")) {
				membersCerts.put(member,  certFile);
			}
		}
		
		if (!membersCerts.isEmpty()) {
			sendCertificatesToMembers(membersCerts, emailTo);
		}
		
		return memberService.getAll();
	}

	public String generateMemberCertificate(Member member) throws IOException {
		String certFileName = String.format("VillageGreensMemberCertificate-%d.pdf", member.getMemberNo());
		FileOutputStream fos = new FileOutputStream(String.format("/Users/john/dev/" + certFileName, member.getMemberNo()));
		byte[] certificateBytes = generateCertificateFromTemplate(member, "VG-Share-Certificate-04.pdf");
		fos.write(certificateBytes);
		fos.close();
		return certFileName;
	}
	
	public void sendCertificatesToMembers(Map<Member, String> membersCerts, String emailTo) {
		
		List<MimeMessage> messageList = new ArrayList<MimeMessage>();
		try {
			final JavaMailSender javaMailSender = (JavaMailSender)mailSender;
			for (Map.Entry<Member, String> memberCert : membersCerts.entrySet()) {
	
				Member member = memberCert.getKey();
				String certFileName = memberCert.getValue();
				
				String email = member.getEmail();
				if (email == null || email.equals("")){
					LOG.warn("No email address for certificate {}", member.getMemberNo());
					continue;
				}
				
				final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setFrom("info@village-greens-coop.co.uk", "Village Greens Info");
				String sendingEmailTo = member.getEmail(); 
				if (emailTo != null && !emailTo.equals("")) {
					sendingEmailTo = emailTo;
				}
				message.setTo(sendingEmailTo);
				LOG.info("Sending certificate {} to {}", member.getMemberNo(), sendingEmailTo);
				message.setSubject("Your Village Greens Share Certificate");
				message.setText(member.getSalutation(false) + "\n\n"
						+ "Attached please find your certificate to confirm shares purchased in Village Greens (Prestwich) Co-operative Ltd.\n\n"
						+ "We want to take this opportunity to say a huge thank you for your support and investment, and we are only weeks away from opening the doors on this exciting new community venture as the lease has now been signed.\n\n"
						+ "Together we have made this happen, and this is just the beginning.\n\n\n"
						+ "The Directors of Village Greens\n"
						+ "www.village-greens-coop.co.uk\n\n\n"
						+ "NB If you have not already done so then please let Janet know if you wish to be registered for 50% tax relief with HMRC at: janet@village-greens-coop.co.uk\n\n\n");
				message.addAttachment(certFileName, new File("/Users/john/dev/" + certFileName));
				messageList.add(mimeMessage);
			}
			
			if (messageList.size() > 0) {
				// send the messages in bulk
				LOG.info("Sending {} messages...", messageList.size());
				javaMailSender.send(messageList.toArray(new MimeMessage[messageList.size()]));
				LOG.info("Sent!");
				
				// if all sent successfully, mark as sent
				for (Member member : membersCerts.keySet()) {
					memberService.markCertificateSent(member);
				}
			}
			
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] generateCertificateFromTemplate(Member member, String templateFileName) {
		byte[] retval = null;
		PdfReader reader = null;
		PdfStamper stamper = null;
		
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		String templatePath = "images/" + templateFileName;
		try {
			reader = new PdfReader(new ClassPathResource(templatePath).getInputStream());
            stamper = new PdfStamper(reader, baos);
		} catch (IOException e) {
        	LOG.error("IOException caught reading template {}", templatePath, e);
        	return retval;
		} catch (DocumentException e) {
        	LOG.error("DocumentException caught reading template {}", templatePath, e);
        	return retval;
		}

		// get the content layer for stamping over the existing content
		try {
			PdfContentByte content = stamper.getOverContent(1);
			content.beginText();
			
			BaseFont bfSmall = createBaseFont("DIN-Light.ttf", "images/din-light.ttf");
//			BaseFont bfLarge = createBaseFont("FestivoLettersNo6.otf", "images/FestivoLettersNo.6.otf");

			content.setRGBColorFill(71, 55, 41);

			String memberName = member.getDisplayName();
			if (memberName.length() >= 30) {
				content.setFontAndSize(bfSmall, 22F);
				content.showTextAligned(PdfContentByte.ALIGN_LEFT, 
						memberName,
						344,276,0);
			} else if (memberName.length() > 28) {
				content.setFontAndSize(bfSmall, 22F);
				content.showTextAligned(PdfContentByte.ALIGN_CENTER, 
						memberName,
						504,276,0);
			} else {
				content.setFontAndSize(bfSmall, 24F);
				content.showTextAligned(PdfContentByte.ALIGN_CENTER, 
						memberName,
						504,276,0);
			}

			content.setFontAndSize(bfSmall, 24F);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, 
					new DecimalFormat("###,###").format(member.getTotalInvestment()),
					656,230,0);

			content.setFontAndSize(bfSmall, 16F);
			content.setRGBColorFill(71, 55, 41);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, 
					new DecimalFormat("0").format(member.getMemberNo()),
					718,132,0);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, 
					new DecimalFormat("###,###").format(member.getTotalInvestment()),
					718,98,0);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, 
					String.format("%s", new SimpleDateFormat("dd MMMM yyyy").format(new Date())),
					380,98,0);
			
			content.endText();
			stamper.close();
		} catch (DocumentException e) {
			LOG.error("DocumentException caught writing certificate", e);
			return retval;
		} catch (IOException e) {
			LOG.error("IOException caught writing certificate", e);
			return retval;
		}

        retval = baos.toByteArray();
        return retval;
	}
	
	protected BaseFont createBaseFont(String fontName, String fontFile) {
		try {
			byte[] fontBytes = Utils.getClassPathResourceIntoByteArray(fontFile);
			BaseFont bf = BaseFont.createFont(fontName, BaseFont.WINANSI, BaseFont.EMBEDDED, BaseFont.NOT_CACHED, fontBytes, null);
			return bf;
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
