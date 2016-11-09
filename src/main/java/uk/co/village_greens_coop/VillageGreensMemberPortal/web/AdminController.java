package uk.co.village_greens_coop.VillageGreensMemberPortal.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.co.village_greens_coop.VillageGreensMemberPortal.form.DocumentForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.MemberForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.SendStockEmailForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.StockEmailForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.validation.SendStockEmailFormValidator;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Dashboard;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Document;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.StockEmail;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.DocumentRows;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRows;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.StockEmailRows;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.DashboardService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.DocumentApiService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.DocumentService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.EmailApiService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.EmailService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.MemberAPIService;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.MemberService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	
	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private DashboardService dashboardService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberAPIService memberAPIService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private EmailApiService emailApiService;
	
	@Autowired
	private DocumentService documentService;

	@Autowired
	private DocumentApiService documentApiService;
	
	@Autowired
	private SendStockEmailFormValidator sendStockEmailFormValidator;

	//    @InitBinder("memberForm")
//    private void initBinder(WebDataBinder binder) {
//    	// we want standard validation, plus also a special TelephoneFormValidator
//    	binder.setValidator(new MemberFormValidator(new TelephoneFormValidator()));
//    }

    @InitBinder("sendStockEmailForm")
    private void initBinder(WebDataBinder binder) {
	  	// we want standard validation, plus also a special validator for the sendStockEmailForm
	  	binder.setValidator(sendStockEmailFormValidator);
    }
    
	@ModelAttribute
	public Dashboard getDashboardValues() {
    	Dashboard dashboard = dashboardService.getDashboardFigures();
    	return dashboard;
	}
	
    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getAdminDashboard(HttpServletRequest request, Model model) {
    	return "admin/dashboard";
    }

	@RequestMapping(value = "allMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getAllMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "ALL");
    	request.getSession().setAttribute("memberStatus", "ALL");
    	addMemberModelAttributes("ALL", model);
    	return "admin/members";
    }
    
    @RequestMapping(value = "fullMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getFullMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "FULL");
    	request.getSession().setAttribute("memberStatus", "FULL");
    	addMemberModelAttributes("FULL", model);
    	return "admin/members";
    }
    
    @RequestMapping(value = "certifiableMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getCertifiableMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "CERTIFIABLE");
    	request.getSession().setAttribute("memberStatus", "CERTIFIABLE");
    	addMemberModelAttributes("CERTIFIABLE", model);
    	return "admin/members";
    }
    
    @RequestMapping(value = "partialMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getPartialMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "PART");
    	request.getSession().setAttribute("memberStatus", "PART");
    	addMemberModelAttributes("PART", model);
    	return "admin/nonFullMembers";
    }
    
    @RequestMapping(value = "unpaidMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getUnpaidMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "UNPAID");
    	request.getSession().setAttribute("memberStatus", "UNPAID");
    	addMemberModelAttributes("UNPAID", model);
    	return "admin/nonFullMembers";
    }
    
    @RequestMapping(value = "overduePayments", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getMembersWithOverduePayments(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "OVERDUE");
    	request.getSession().setAttribute("memberStatus", "OVERDUE");
    	addMemberModelAttributes("OVERDUE", model);
    	return "admin/nonFullMembers";
    }
    
    @RequestMapping(value = "committeeMembers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getCommitteeMembers(HttpServletRequest request, Model model) {
    	model.addAttribute("memberStatus", "COMMITTEE");
    	request.getSession().setAttribute("memberStatus", "COMMITTEE");
    	addMemberModelAttributes("COMMITTEE", model);
    	return "admin/members";
    }
    
    @RequestMapping(value = "memberRows", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody MemberRows getMemberRows(
    			@RequestParam(value = "memberStatus") String memberStatus, 
    			HttpServletRequest request) {
    	return memberAPIService.getMemberRows(memberStatus);
    }
    
    @RequestMapping(value = "downloadMembers.xlsx", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response) throws Exception {

		return new ModelAndView("ExcelMembersView","members", memberService.getAll());
	}
    
    private void addMemberModelAttributes(String memberStatus, Model model) {
    	String returnUrl = "/admin/";
    	String breadcrumbMembersDescription = " Members";
    	
    	if (memberStatus.equals("ALL")) {
    		returnUrl += "allMembers";
    		breadcrumbMembersDescription = "All" + breadcrumbMembersDescription;
    	} else if (memberStatus.equals("PART")) {
    		returnUrl += "partialMembers";
    		breadcrumbMembersDescription = "Part-paid" + breadcrumbMembersDescription;
    	} else if (memberStatus.equals("FULL")) {
    		returnUrl += "fullMembers";
    		breadcrumbMembersDescription = "Full" + breadcrumbMembersDescription;
    	} else if (memberStatus.equals("CERTIFIABLE")) {
    		returnUrl += "certifiableMembers";
    		breadcrumbMembersDescription = "Uncertified Full" + breadcrumbMembersDescription;
    	} else if (memberStatus.equals("UNPAID")) {
    		returnUrl += "unpaidMembers";
    		breadcrumbMembersDescription = "Unpaid" + breadcrumbMembersDescription;
    	} else if (memberStatus.equals("OVERDUE")) {
    		returnUrl += "overduePayments";
    		breadcrumbMembersDescription = "Overdue" + breadcrumbMembersDescription;
    	} else if (memberStatus.equals("COMMITTEE")) {
    		returnUrl += "committeeMembers";
    		breadcrumbMembersDescription = "Committe" + breadcrumbMembersDescription;
    	} else if (memberStatus.equals("NEW")) {
    		returnUrl += "addMember";
    		breadcrumbMembersDescription = "Add Member";
    	}
    	
    	model.addAttribute("returnUrl", returnUrl);
    	model.addAttribute("breadcrumbMembersDescription", breadcrumbMembersDescription);
    }

    @RequestMapping(value = "member", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getMember(
    		@RequestParam(value = "id") Long id,
    		@RequestParam(value = "memberStatus") String memberStatus,
    		HttpServletRequest request, Model model) {

    	Member member = memberService.getById(id);
    	if (member != null) {
    		MemberForm mf = new MemberForm(member);
        	model.addAttribute("memberForm", mf);
        	request.getSession().setAttribute("memberId", id);
        	addMemberModelAttributes(memberStatus, model);
        	
        	model.addAttribute("canCertify", Boolean.toString(memberService.canCertify(id)));
        	model.addAttribute("canRecertify", Boolean.toString(memberService.canRecertify(id)));
        	return "admin/member";
    	} else {
    		//TODO add an error message, the id wasn't found
    		return "redirect:allMembers";
    	}
    }
    
    @RequestMapping(value = "addMember", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String addMember(
    		HttpServletRequest request, Model model) {

   		MemberForm mf = new MemberForm();
        model.addAttribute("memberForm", mf);
        addMemberModelAttributes("NEW", model);
        return "admin/member";
    }
    
    @RequestMapping(value = "member", method = RequestMethod.POST)
    public String saveMember(
    		@Valid @ModelAttribute (value = "memberForm") MemberForm memberForm,
			BindingResult result, 
			Errors errors, 
			RedirectAttributes ra,
			Model model,
			HttpServletRequest request) {
    	
    	// perform deeper validation of the member form (particularly the nested list of TelephoneForms
//    	MemberFormValidator mfv = new MemberFormValidator(new TelephoneFormValidator());
//    	mfv.validate(memberForm, errors);
    	
    	if (errors.hasErrors()) {
    		LOG.info("Errors in Member form");
    		List<FieldError> fieldErrors = errors.getFieldErrors();
    		
    		// look for any telephone field errors
    		for (FieldError fieldError: fieldErrors) {
    			if (fieldError.getField().startsWith("telephones")) {
    	    		model.addAttribute("telephoneErrors", "true");
    			} else if (fieldError.getField().startsWith("payments")) {
    	    		model.addAttribute("paymentErrors", "true");
    			}
    		}
    			
    		return "admin/member";
    	}

    	if (memberForm.getUpdateState().equals("U")) {
        	memberService.updateMemberFromForm(memberForm);
    	} else if (memberForm.getUpdateState().equals("N")) {
        	memberService.createMemberFromForm(memberForm);
    	}
    	
    	String memberStatus = (String)request.getSession().getAttribute("memberStatus");
    	String returnString = "allMembers";
    	
    	if (memberStatus != null) {
    		if (memberStatus.equals("FULL")) {
    			returnString = "fullMembers";
    		} else if (memberStatus.equals("PART")) {
    			returnString = "partialMembers";
    		} else if (memberStatus.equals("UNPAID")) {
    			returnString = "unpaidMembers";
    		} else if (memberStatus.equals("COMMITTEE")) {
    			returnString = "committeeMembers";
    		}
    	}
    	
    	return "redirect:/admin/" + returnString;
    }
    
    @RequestMapping(value = "certifyMember", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String certifyMember(
    		@RequestParam(value = "id") Long id,
    		HttpServletRequest request, Model model) {

    	if (memberService.canCertify(id)) {
    		Member member = memberService.certifyMember(id);
    		MemberForm mf = new MemberForm(member);
        	model.addAttribute("memberForm", mf);
        	request.getSession().setAttribute("memberId", id);
        	addMemberModelAttributes((String)request.getSession().getAttribute("memberStatus"), model);
        	return "admin/member";
    	} else {
    		//TODO add an error message, the id wasn't found
    		return "redirect:allMembers";
    	}
    }

    @RequestMapping(value = "recertifyMember", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String recertifyMember(
    		@RequestParam(value = "id") Long id,
    		HttpServletRequest request, Model model) {

    	if (memberService.canRecertify(id)) {
    		Member member = memberService.recertifyMember(id);
    		MemberForm mf = new MemberForm(member);
        	model.addAttribute("memberForm", mf);
        	request.getSession().setAttribute("memberId", id);
        	addMemberModelAttributes((String)request.getSession().getAttribute("memberStatus"), model);
        	return "admin/member";
    	} else {
    		//TODO add an error message, the id wasn't found
    		return "redirect:allMembers";
    	}
    }

    @RequestMapping(value = "certificate", method = RequestMethod.GET )
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public FileSystemResource viewCertificate(
    		@RequestParam(value = "id") Long id,
    		HttpServletRequest request, 
    		HttpServletResponse response,
    		Model model) {

    	return memberService.getCertificateForDownload(id, response);
    }

    @RequestMapping(value = "document", method = RequestMethod.GET )
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public FileSystemResource downloadDocument(
    		@RequestParam(value = "id") Long id,
    		HttpServletRequest request, 
    		HttpServletResponse response,
    		Model model) {

    	return documentService.getDocumentForDownload(id, response);
    }

    @RequestMapping(value = "emails", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getEmailsList(HttpServletRequest request, Model model) {
//    	LOG.info("WAYMARK - calling getAllEmails();...");
//    	List<StockEmailForm> emailForms = emailService.getAllEmails();
//    	List<StockEmailForm> cutDownForms = new ArrayList<StockEmailForm>();
//    	cutDownForms.add(emailForms.get(0));
//    	cutDownForms.add(emailForms.get(1));
//    	LOG.info("WAYMARK - called getAllEmails();...");
//    	LOG.info("WAYMARK - adding emailForms to model...");
//    	model.addAttribute("emails", emailForms);
//    	LOG.info("WAYMARK - added emailForms to model");
    	return "admin/emails";
    }

    @RequestMapping(value = "emailRows", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody StockEmailRows getStockEmailRows(HttpServletRequest request) {
    	return emailApiService.getStockEmails();
    }
    
    @RequestMapping(value = "addEmail", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String addEmail(
    		HttpServletRequest request, Model model) {

   		StockEmailForm sef = emailService.getStockEmailForm();
        model.addAttribute("stockEmailForm", sef);
        return "admin/email";
    }
    
    @RequestMapping(value = "editEmail", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getStockEmail(
    		@RequestParam(value = "id") Long id,
    		HttpServletRequest request, Model model) {

    	StockEmail stockEmail = emailService.getStockEmailById(id);
    	if (stockEmail != null) {
    		StockEmailForm sef = emailService.getStockEmailForm(stockEmail);
        	model.addAttribute("stockEmailForm", sef);
        	request.getSession().setAttribute("stockEmailId", id);
        	
        	return "admin/email";
    	} else {
    		//TODO add an error message, the id wasn't found
    		return "redirect:emails";
    	}
    }
    
    @RequestMapping(value = "deleteEmail", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteStockEmail(
    		@RequestParam(value = "id") Long id,
    		HttpServletRequest request, 
    		RedirectAttributes ra, 
    		Model model) {

    	String emailPurpose = emailService.deleteStockEmail(id);
    	ra.addFlashAttribute("message", String.format("%s email deleted successfully", emailPurpose));
    	return "redirect:emails";
    }
    
    @RequestMapping(value = "email", method = RequestMethod.POST)
    public String saveEmail(
    		@Valid @ModelAttribute (value = "stockEmailForm") StockEmailForm stockEmailForm,
			BindingResult result, 
			Errors errors, 
			RedirectAttributes ra,
			Model model,
			HttpServletRequest request) {
    	
    	if (errors.hasErrors()) {
    		LOG.info("Errors in StockEmailForm");
    		List<FieldError> fieldErrors = errors.getFieldErrors();
    		
    		return "admin/email";
    	}

    	if (stockEmailForm.getUpdateState().equals("U")) {
        	emailService.updateStockEmail(stockEmailForm);
    	} else if (stockEmailForm.getUpdateState().equals("N")) {
        	emailService.createStockEmail(stockEmailForm);
    	}
    	
    	return "redirect:/admin/emails";
    }
 
    @RequestMapping(value = "sendEmail", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getSendEmailForm(
    		@RequestParam(value = "id") Long id,
    		HttpServletRequest request, Model model) {

    	StockEmail stockEmail = emailService.getStockEmailById(id);
    	if (stockEmail != null) {
    		SendStockEmailForm sef = new SendStockEmailForm(stockEmail);
        	model.addAttribute("sendStockEmailForm", sef);
        	
        	return "admin/sendEmail";
    	} else {
    		//TODO add an error message, the id wasn't found
    		return "redirect:/admin/emails";
    	}
    }

    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    public String sendEmail(
    		@Valid @ModelAttribute (value = "sendStockEmailForm") SendStockEmailForm sendStockEmailForm,
			BindingResult result, 
			Errors errors, 
			RedirectAttributes ra,
			Model model,
			HttpServletRequest request) {
    	
    	if (errors.hasErrors()) {
    		LOG.info("Errors in SendStockEmailForm");
//    		List<FieldError> fieldErrors = errors.getFieldErrors();
    		
    		return "admin/sendEmail";
    	}

    	String emailPurpose = sendStockEmailForm.getEmailPurpose();
    	int numRequested = emailService.sendStockEmail(sendStockEmailForm);
    	
    	if (numRequested != -1) {
    		ra.addFlashAttribute("message", String.format("%s email queued successfully for %d recipient(s)", emailPurpose, numRequested));
    	}
    	return "redirect:emails";
    }
    
    @RequestMapping(value = "documents", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String getDocumentsList(HttpServletRequest request, Model model) {
//    	List<DocumentForm> documentForms = documentService.getAllDocumentsAsForms();
//    	model.addAttribute("documents", documentForms);
    	return "admin/documents";
    }

    @RequestMapping(value = "documentRows", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody DocumentRows getDocumentRows(HttpServletRequest request) {
    	return documentApiService.getAllDocumentRows();
    }
    
    @RequestMapping(value = "addDocument", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String addDocument(
    		HttpServletRequest request, Model model) {

   		DocumentForm df = new DocumentForm();
        model.addAttribute("documentForm", df);
        return "admin/document";
    }

    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/document", method = RequestMethod.POST, consumes = { "multipart/form-data" })
      public String uploadFileHandler(
    		@RequestParam("description") String description,
            @RequestParam("filename") MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
 
                // Create the file on server
                File serverFile = new File(
                		DocumentService.DOCUMENT_LOCATION + file.getOriginalFilename());
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
 
                LOG.info("File uploaded: " + serverFile.getAbsolutePath());
                
                documentService.createDocument(file.getOriginalFilename(), description);
 
            } catch (Exception e) {
                LOG.error("Upload of {} failed", file.getName(), e);
            }
        } else {
            LOG.warn("File upload not possible due to empty file");
        }
        return "redirect:documents";
    }

    @RequestMapping(value = "deleteDocument", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteDocument(
    		@RequestParam(value = "id") Long id,
    		RedirectAttributes ra,
    		HttpServletRequest request, Model model) {

    	Document doc = documentService.deleteDocument(id);
    	String documentDescription = doc.getDescription();
    	ra.addFlashAttribute("message", String.format("%s deleted successfully", documentDescription));

    	return "redirect:documents";
    }
    
}
