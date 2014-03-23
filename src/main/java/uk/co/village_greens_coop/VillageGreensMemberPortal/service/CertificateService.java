package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.utils.Utils;

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

	@Autowired
	private MailSender mailSender;

	public byte[] generateCertificate(Member member) {
		System.out.println("Called cert service generateCertificate() for member " + member);
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
            	System.out.println("IOException caught: " + ioe.getMessage());
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
	
	public void generateMemberCertificates(List<Member> membersList) {
		for (Member member : membersList) {
			String certFile = null;
			try {
				certFile = generateMemberCertificate(member);
			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
			if (certFile != null) {
//				sendCertificateToMember(member, certFile);
			}
		}
	}

	public String generateMemberCertificate(Member member) throws IOException {
		String certFileName = String.format("VillageGreensMemberCertificate-%d.pdf", member.getMemberNo());
		FileOutputStream fos = new FileOutputStream(String.format("/Users/john/dev/" + certFileName, member.getMemberNo()));
		byte[] certificateBytes = generateCertificateFromTemplate(member, "VG-Share-Certificate-02.pdf");
		fos.write(certificateBytes);
		fos.close();
		return certFileName;
	}
	
	public void sendCertificateToMember(Member member, String certFileName) {
		try {
			final JavaMailSender javaMailSender = (JavaMailSender)mailSender;
			final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
			message.setFrom("jhurst1970@gmail.com");
//			message.setTo(member.getEmail());
			message.setTo("denise@village-greens-coop.co.uk");
			message.setSubject("Your Village Greens Share Certificate");
			message.setText(String.format("Dear %s %s", member.getTitle(), member.getSurname()) + "\n\n"
					+ "Please find attached your Village Greens Cooperative share certificate.\n\n"
					+ "Yours faithfully\n\n"
					+ "Village Greens Treasurer\n");
			message.addAttachment(certFileName, new File("/Users/john/dev/" + certFileName));
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public byte[] generateCertificateFromTemplate(Member member, String templateFileName) {
		System.out.println("Called cert service generateCertificateFromTemplate() \n" + member);
		byte[] retval = null;
		PdfReader reader = null;
		PdfStamper stamper = null;
		
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		String templatePath = "images/" + templateFileName;
		try {
			reader = new PdfReader(new ClassPathResource(templatePath).getInputStream());
            stamper = new PdfStamper(reader, baos);
		} catch (IOException e) {
        	System.out.println("IOException caught reading template " + templatePath + ": " + e.getMessage());
        	return retval;
		} catch (DocumentException e) {
        	System.out.println("DocumentException caught reading template " + templatePath + ": " + e.getMessage());
        	return retval;
		}

		// get the content layer for stamping over the existing content
		try {
			PdfContentByte content = stamper.getOverContent(1);
			content.beginText();
			
			BaseFont bfSmall = createBaseFont("DIN-Light.ttf", "images/din-light.ttf");
			BaseFont bfLarge = createBaseFont("FestivoLettersNo6.otf", "images/FestivoLettersNo.6.otf");
			content.setFontAndSize(bfLarge, 24F);
			content.setRGBColorFill(71, 55, 41);
			content.showTextAligned(PdfContentByte.ALIGN_CENTER, 
					String.format("%s %s", member.getFirstName(), member.getSurname()),
					504,276,0);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, 
					new DecimalFormat("###,###").format(member.getTotalInvestment()),
					656,230,0);

			content.setFontAndSize(bfSmall, 16F);
			content.setRGBColorFill(71, 55, 41);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, 
					new DecimalFormat("00000").format(member.getMemberNo()),
					718,132,0);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, 
					new DecimalFormat("###,###").format(member.getTotalInvestment()),
					718,98,0);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, 
					String.format("%s", new SimpleDateFormat("dd MMMM yyyy").format(new Date())),
					380,98,0);
			
			content.endText();
			stamper.close();
		} catch (DocumentException e1) {
			System.out.println("DocumentException caught creating font");
			e1.printStackTrace();
			return retval;
		} catch (IOException e1) {
			System.out.println("IOException caught creating font");
			e1.printStackTrace();
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
