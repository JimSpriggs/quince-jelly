package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MemberTelephoneDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.MembershipPaymentDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.PaymentMethodDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.TelephoneTypeDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.MemberForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.PaymentForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.TelephoneForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MemberTelephone;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.MembershipPayment;

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
	private MembershipPaymentDao membershipPaymentRepository;

	@Autowired
	private TelephoneTypeDao telephoneTypeRepository;

	@Autowired
	private PaymentMethodDao paymentMethodRepository;

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
	
	public List<Member> getAllAwaitingCertificate() {
		return memberRepository.getAllAwaitingCertificate();
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
	
	@Transactional
	public Member createMemberFromForm(MemberForm mf) {
		Member member = new Member();
		updateMember(member, mf);
		memberRepository.save(member);
		return member;
	}
	
	private boolean hasChanged(String memberVal, String formVal) {
		if (memberVal == null && formVal == null) {
			return false;
		}
		if (memberVal == null && formVal != null) {
			return true;
		}
		if (memberVal != null && formVal == null) {
			return true;
		}
		if (memberVal.trim().equals(formVal.trim())) {
			return false;
		}
		return true;
	}
	
	private boolean hasChanged(BigDecimal memberVal, Integer formVal) {
		if (memberVal == null && formVal == null) {
			return false;
		}
		if (memberVal == null && formVal != null) {
			return true;
		}
		if (memberVal != null && formVal == null) {
			return true;
		}
		if (memberVal.intValue() == formVal.intValue()) {
			return false;
		}
		return true;
	}
	
    private void updateMember(Member member, MemberForm mf) {
    	if (hasChanged(member.getTitle(), mf.getTitle()) ||
    			hasChanged(member.getFirstName(), mf.getFirstName()) ||
    			hasChanged(member.getSurname(), mf.getSurname()) ||
    			hasChanged(member.getOrganisation(), mf.getOrganisation()) ||
    			hasChanged(member.getTotalInvestment(), mf.getTotalInvestment())) {
    		// need to reissue certificate, so set its generated date to null
    		member.setCertificateGenerated(null);
    	}
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
    	if (mf.getPayments() != null) {
	    	for (PaymentForm pf : mf.getPayments()) {
	    		MembershipPayment mp = null;
	    		
	    		if (pf.getId() != null) {
	    			if (pf.getId().longValue() != 0) {
	    				mp = locateMembershipPayment(member, pf.getId());
		    			if ("D".equals(pf.getUpdateState())) {
		    				member.deletePayment(mp);
		    				membershipPaymentRepository.delete(mp);
		    			} else if ("U".equals(pf.getUpdateState())) {
		    				mp.setPaymentAmount(pf.getAmount());
		    				mp.setDueDate(pf.getDueDate());
		    				mp.setReceivedDate(pf.getReceivedDate());
		    				mp.setPaymentMethod(paymentMethodRepository.findByMethod(pf.getPaymentMethod()));
		    			}
	    			} else if ("N".equals(pf.getUpdateState())) {
	    				if (pf.getAmount() != null) {
	    					mp = member.addNewPayment(pf.getAmount(), pf.getDueDate(), pf.getReceivedDate(), paymentMethodRepository.findByMethod(pf.getPaymentMethod()));
	    				}
	    			}
	    		} else {
	    			LOG.error("Null value detected of MembershipPayment.id");
	    		}
	    	}
    	}
    	
    	if (member.getMembershipPayments() != null) {
    		BigDecimal totalPayments = new BigDecimal(0L);
    		for (MembershipPayment mp : member.getMembershipPayments()) {
    			if (mp.getReceivedDate() != null) {
    				totalPayments = totalPayments.add(mp.getPaymentAmount());
    			}
    		}
    		
    		if (totalPayments.compareTo(new BigDecimal(0L)) == 0) {
    			member.setMemberStatus("UNPAID");
    		} else {
    			if (totalPayments.compareTo(member.getTotalInvestment()) >= 0) {
        			member.setMemberStatus("FULL");
    			} else {
        			member.setMemberStatus("PART");
    			}
    		}
    	} else {
			member.setMemberStatus("UNPAID");
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

    private MembershipPayment locateMembershipPayment(Member member, Long id) {
    	if (member.getMembershipPayments() != null) {
    		for (MembershipPayment mp : member.getMembershipPayments()) {
    			if (mp.getId().equals(id)) {
    				return mp;
    			}
    		}
    	}
    	return null;
    }
    
	@Transactional
	public Member certifyMember(long id) {
		Member member = getById(id);
		if (member.getMemberno() == null) {
			memberRepository.generateMemberNoAndSave(member);
		}
		member.setCertificateGenerated(new Date());
		memberRepository.save(member);
		if (certificateService.generateMemberCertificate(member)) {
			return member;
		}
		return null;
	}
	
	public FileSystemResource getCertificateForDownload(long id, HttpServletResponse response) {
		FileSystemResource fsr = null;
		
		Member member = getById(id);
		if (member != null) {
			String fileName = certificateService.getCertificateFileName(member);
			response.setHeader( "Content-Disposition", "attachment;filename=" + fileName);
			fsr = new FileSystemResource(certificateService.getCertificateFullFileName(member));
		}
		return fsr;
	}
	
}
