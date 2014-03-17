package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class CertificateService {

	public void generateCertificate(HttpServletRequest request) {
		System.out.println("Called cert service generateCertificate()");
		Document document = new Document(PageSize.A4.rotate());
		
        try {
            PdfWriter.getInstance(document,
                new FileOutputStream("/Users/john/dev/Paragraph2.pdf"));

            String imageUrl = request.getRequestURL() + "/../resources/images/Certificate-VG-Logo.png";

            document.open();

            try {
            	Image image2 = Image.getInstance(new URL(imageUrl));
                document.add(image2);
            } catch (IOException ioe) {
            	System.out.println("IOException caught: " + ioe.getMessage());
            }
            
            Paragraph paragraph1 = new Paragraph();
            
            Paragraph paragraph2 = new Paragraph();

            paragraph2.setSpacingAfter(25);
            paragraph2.setSpacingBefore(25);
            paragraph2.setAlignment(Element.ALIGN_CENTER);
            paragraph2.setIndentationLeft(50);
            paragraph2.setIndentationRight(50);
            

            for(int i=0; i<10; i++){
                Chunk chunk = new Chunk(
                    "This is a sentence which is long " + i + ". ");
                paragraph1.add(chunk);
                paragraph2.add(chunk);
            }

            document.add(paragraph1);
            document.add(paragraph2);
            
            document.close(); // no need to close PDFwriter?

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		
	}
}
