package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;

@Service
public class MemberService {

	@Autowired
	private CertificateService certificateService;
	
	@Autowired
	private MemberDao memberRepository;
	
	@PostConstruct	
	protected void initialize() {
//		memberRepository.save(
//				new Member("Ms", "Denise", "McAvoy", "denise@village-greens-coop.co.uk", "76 Clifton Road", "Prestwich", "Manchester", null, "M25 3HR", "19/05/1970", new BigDecimal(10000), true));
//		memberRepository.save(
//			new Member("Mr", "John", "Hurst", "jhurst1970@gmail.com", "76 Clifton Road", "Prestwich", "Manchester", null, "M25 3HR", "18/07/1970", new BigDecimal(500), true));
//		memberRepository.save(
//			new Member("Mrs", "Rebecca", "Hurst", "rebeccajphillips8@gmail.com", "76 Clifton Road", "Prestwich", "Manchester", null, "M25 3HR", "19/05/1970", new BigDecimal(500), true));
//		memberRepository.save(
//				new Member("Mr", "Test", "Member", "whoever@wherever.com", "76 Clifton Road", "Prestwich", "Manchester", null, "M25 3HR", "19/05/1970", new BigDecimal(99999), true));
		
		
	}
	
	public List<Member> getAll() {
		List<Member> membersList = memberRepository.getAll();
		
		return membersList;
	}

	public List<Member> getAllEmailable() {
		return memberRepository.getAllEmailable();
	}
	
}
