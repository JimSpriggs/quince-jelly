package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;

@Service
public class MemberService {

	@Autowired
	private CertificateService certificateService;
	
	@Autowired
	private MemberDao memberRepository;
	
	@Autowired
	private MailSender mailSender;
	
	@PostConstruct	
	protected void initialize() {
		memberRepository.save(
				new Member("Ms", "Denise", "McAvoy", "denise@village-greens-coop.co.uk", "76 Clifton Road", "Prestwich", "Manchester", null, "M25 3HR", "19/05/1970", 10000L, true));
		memberRepository.save(
			new Member("Mr", "John", "Hurst", "jhurst1970@gmail.com", "76 Clifton Road", "Prestwich", "Manchester", null, "M25 3HR", "18/07/1970", 500L, true));
		memberRepository.save(
			new Member("Mrs", "Rebecca", "Hurst", "rebeccajphillips8@gmail.com", "76 Clifton Road", "Prestwich", "Manchester", null, "M25 3HR", "19/05/1970", 500L, true));
		memberRepository.save(
				new Member("Mr", "Test", "Member", "whoever@wherever.com", "76 Clifton Road", "Prestwich", "Manchester", null, "M25 3HR", "19/05/1970", 99999L, true));
		
		
	}
	
	public List<Member> getAll() {
		List<Member> membersList = memberRepository.getAll();
		
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
				sendCertificateToMember(member, certFile);
			}
		}
		
		return membersList;
	}

	public List<Member> getAllEmailable() {
		return memberRepository.getAllEmailable();
	}
	
	public String generateMemberCertificate(Member member) throws IOException {
		String certFileName = String.format("VillageGreensMemberCertificate-%d.pdf", member.getMemberNo());
		FileOutputStream fos = new FileOutputStream(String.format("/Users/john/dev/" + certFileName, member.getMemberNo()));
		byte[] certificateBytes = certificateService.generateCertificateFromTemplate(member, "certificate-draft.pdf");
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
}
