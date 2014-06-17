package uk.co.village_greens_coop.VillageGreensMemberPortal.support.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Account;
import uk.co.village_greens_coop.VillageGreensMemberPortal.service.AccountService;

@Component
class AuthenticationApplicationListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationApplicationListener.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private HttpSession httpSession;
	
	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		Authentication auth = event.getAuthentication();
		Account account = null;
		
		if (auth != null) {
			account = accountService.getAccountByEmailAddress(auth.getName());
			if (account != null) {
				httpSession.setAttribute("currentAccount", account);
				LOG.info("Successful authentication by {} {}", account.getFirstName(), account.getSurname());
				
			}
		}
	}
}