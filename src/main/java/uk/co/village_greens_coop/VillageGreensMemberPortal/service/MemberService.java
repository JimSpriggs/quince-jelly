package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberRepository;
	
	@PostConstruct	
	protected void initialize() {
		memberRepository.save(
			new Member("Mr", "John", "Hurst", "jhurst1970@gmail.com", "76 Clifton Road", "Prestwich", "Manchester", null, "M25 3HR", "18/07/1970", 500L, true));
		memberRepository.save(
			new Member("Mrs", "Rebecca", "Hurst", "rebeccajphillips8@gmail.com", "76 Clifton Road", "Prestwich", "Manchester", null, "M25 3HR", "19/05/1970", 500L, true));
	}
	
	public List<Member> getAll() {
		return memberRepository.getAll();
	}

	public List<Member> getAllEmailable() {
		return memberRepository.getAllEmailable();
	}
}
