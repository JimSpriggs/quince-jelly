package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;

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

	public byte[] generateCertificate(Member member) {
		System.out.println("Called cert service generateCertificate()");
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
	            BufferedInputStream bis = new BufferedInputStream(
	            							new ClassPathResource("images/Certificate-VG-Logo.png")
	            								.getInputStream());
	            byte[] imageBytes = new byte[100000];
	            bis.read(imageBytes);
	            image = Image.getInstance(imageBytes);
	            bis.close();

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
	
	public byte[] generateCertificateFromTemplate(Member member, String templateFileName) {
		System.out.println("Called cert service generateCertificateFromTemplate()");
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
			content.setFontAndSize(
					BaseFont.createFont(BaseFont.HELVETICA_BOLD, 
										BaseFont.WINANSI, 
										BaseFont.EMBEDDED), 
										14F);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, 
					new DecimalFormat("00000").format(member.getMemberNo()),
					370,312,0);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, 
					new DecimalFormat("###,###").format(member.getTotalInvestment()),
					370,285,0);
			content.showTextAligned(PdfContentByte.ALIGN_CENTER, 
					String.format("%s %s", member.getFirstName(), member.getSurname()),
					420,195,0);
			content.showTextAligned(PdfContentByte.ALIGN_CENTER, 
					new DecimalFormat("###,###.00").format(member.getTotalInvestment()),
					420,153,0);
			content.showTextAligned(PdfContentByte.ALIGN_CENTER, 
					String.format("%s", new SimpleDateFormat("dd MMMM yyyy").format(new Date())),
					350,71,0);
			
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
}
