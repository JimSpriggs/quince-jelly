package uk.co.village_greens_coop.VillageGreensMemberPortal.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.co.village_greens_coop.VillageGreensMemberPortal.service.CertificateService;

@Controller
public class CertificateController {

	@Autowired
	private CertificateService certificateService;
	
    @RequestMapping(value = "/certificate", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
//    @ResponseBody
    public String generateCertificates(HttpServletRequest request) {
    	certificateService.generateCertificate(request);
    	return "members/members";
    }

}
