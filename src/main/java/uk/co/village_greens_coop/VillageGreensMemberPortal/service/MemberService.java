package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRow;

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
	
	private List<MemberRow> convertMembersToMemberRows(List<Member> members) {
		List<MemberRow> memberRows = new ArrayList<MemberRow>();
		if (members != null) {
			for (Member member: members) {
				memberRows.add(new MemberRow(member));
			}
		}
		return memberRows;
	}

	public List<MemberRow> getAll() {
		List<Member> membersList = memberRepository.getAll();

		return convertMembersToMemberRows(membersList);
	}

	public List<MemberRow> getAllMemberRowsForDownload() {
		return memberRepository.getAllMemberRowsForDownload();
	}

	public List<MemberRow> getByMemberStatus(String memberStatus) {
		List<Member> membersList = memberRepository.getByMemberStatus(memberStatus);
		
		return convertMembersToMemberRows(membersList);
	}

	public List<MemberRow> getAllEmailable() {
		List<Member> membersList = memberRepository.getAllEmailable();
		
		return convertMembersToMemberRows(membersList);
	}
	
	public List<MemberRow> getAllAwaitingCertificate() {
		List<Member> membersList = memberRepository.getAllAwaitingCertificate();
		
		return convertMembersToMemberRows(membersList);
	}
	
	public List<MemberRow> getAllOverdue() {
		return memberRepository.getOverdueMemberRows();
	}
	
	public List<MemberRow> getUnpaidMembers() {
		return memberRepository.getUnpaidMemberRows();
	}
	
	public List<MemberRow> getPartPaidMembers() {
		return memberRepository.getPartPaidMemberRows();
	}
	
	public List<MemberRow> getCommitteeMembers() {
		return memberRepository.getCommitteeMemberRows();
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
    	member.setCommittee(mf.getCommittee());
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
		if (member != null) {
			if (member.getMemberno() == null) {
				memberRepository.generateMemberNoAndSave(member);
			}
			member.setCertificateGenerated(new Date());
			memberRepository.save(member);
			if (certificateService.generateMemberCertificate(member)) {
				certificateService.sendCertificateToMember(member);
				return member;
			}
		}
		return null;
	}
	
	@Transactional
	public Member recertifyMember(long id) {
		Member member = getById(id);
		if (member != null) {
			member.setCertificateGenerated(new Date());
			memberRepository.save(member);
			if (certificateService.generateMemberCertificate(member)) {
				certificateService.sendCertificateToMember(member);
				return member;
			}
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

	// can certify only if the member is a full member, and there is no existing certificate
	public boolean canCertify(long id) {
		Member member = getById(id);
		return (member != null && 
					"FULL".equals(member.getMemberStatus()) &&
					member.getCertificateGenerated() == null);
	}

	// can recertify only if the member is a full member, and there is an existing certificate
	public boolean canRecertify(long id) {
		Member member = getById(id);
		return (member != null && 
					"FULL".equals(member.getMemberStatus()) &&
					member.getCertificateGenerated() != null);
	}
}
