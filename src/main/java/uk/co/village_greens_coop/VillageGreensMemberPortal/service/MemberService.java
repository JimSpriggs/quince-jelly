package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberTelephoneDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.TelephoneTypeDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.MemberForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.TelephoneForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MemberTelephone;

@Service
@Transactional(readOnly = true)
public class MemberService {

	private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);
	
	@Autowired
	private CertificateService certificateService;
	
	@Autowired
	private MemberDao memberRepository;
	
	@Autowired
	private MemberTelephoneDao memberTelephoneRepository;

	@Autowired
	private TelephoneTypeDao telephoneTypeRepository;

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
	
	@Transactional
	public void markCertificateGenerated(Member member) {
		member.setCertificateGenerated(new Date());
		memberRepository.save(member);
	}

	@Transactional
	public void markCertificateSent(Member member) {
		member.setCertificateSent(new Date());
		memberRepository.save(member);
	}
	
	@Transactional
	public void saveMember(Member member) {
		memberRepository.save(member);
	}
	
	@Transactional
	public Member updateMemberFromForm(MemberForm mf) {
		Member member = getById(mf.getId());
		if (member != null) {
			LOG.info("Updating member id={}", member.getId());
			updateMember(member, mf);
			memberRepository.save(member);
		}
		return member;
	}
	
    private void updateMember(Member member, MemberForm mf) {
    	member.setTitle(mf.getTitle());
    	member.setFirstName(mf.getFirstName());
    	member.setSurname(mf.getSurname());
    	member.setOrganisation(mf.getOrganisation());
    	member.setDob(mf.getDob());
    	member.setEmail(mf.getEmail());
    	member.setAddressLine1(mf.getAddressLine1());
    	member.setAddressLine2(mf.getAddressLine2());
    	member.setAddressLine3(mf.getAddressLine3());
    	member.setAddressLine4(mf.getAddressLine4());
    	member.setPostcode(mf.getPostcode());
    	member.setRollCall(mf.getRollCall());
    	member.setSeis(mf.getSeis());
    	member.setTotalInvestment(new BigDecimal(mf.getTotalInvestment()));
    	if (mf.getTelephones() != null) {
	    	for (TelephoneForm tf : mf.getTelephones()) {
	    		MemberTelephone mt = null;
	    		
	    		if (tf.getId() != null) {
	    			if (tf.getId().longValue() != 0) {
	    				mt = locateMemberTelephone(member, tf.getId());
		    			if ("D".equals(tf.getUpdateState())) {
		    				member.deleteTelephone(mt);
		    				memberTelephoneRepository.delete(mt);
		    			} else if ("U".equals(tf.getUpdateState())) {
		    				mt.setTelephoneNumber(tf.getTelephoneNumber());
		    				mt.setTelephoneType(telephoneTypeRepository.findByType(tf.getTelephoneType()));
		    			}
	    			} else if ("N".equals(tf.getUpdateState())) {
	    				if (!tf.getTelephoneNumber().equals("")) {
	    					mt = member.addNewTelephone(tf.getTelephoneNumber(), telephoneTypeRepository.findByType(tf.getTelephoneType()));
	    				}
	    			}
	    		} else {
	    			LOG.error("Null value detected of MemberTelephone.id");
	    		}
	    	}
    	}
    }
    
    private MemberTelephone locateMemberTelephone(Member member, Long id) {
    	if (member.getMemberTelephones() != null) {
    		for (MemberTelephone mt : member.getMemberTelephones()) {
    			if (mt.getId().equals(id)) {
    				return mt;
    			}
    		}
    	}
    	return null;
    }
}
