package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.util.Date;
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
	}
	
	public Member getById(long id) {
		Member member = memberRepository.find(id);
		return member;
	}
	
	public List<Member> getAll() {
		List<Member> membersList = memberRepository.getAll();
		
		return membersList;
	}

	public List<Member> getByMemberStatus(String memberStatus) {
		List<Member> membersList = memberRepository.getByMemberStatus(memberStatus);
		
		return membersList;
	}

	public List<Member> getAllEmailable() {
		return memberRepository.getAllEmailable();
	}
	
	public List<Member> getAllAwaitingCertificate(int limit) {
		return memberRepository.getAllAwaitingCertificate(limit);
	}
	
	public void markCertificateGenerated(Member member) {
		member.setCertificateGenerated(new Date());
		memberRepository.save(member);
	}

	public void markCertificateSent(Member member) {
		member.setCertificateSent(new Date());
		memberRepository.save(member);
	}
}
