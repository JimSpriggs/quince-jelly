package uk.co.village_greens_coop.VillageGreensMemberPortal.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.co.village_greens_coop.VillageGreensMemberPortal.service.SignUpService;

@Component
public class PasswordResetScavengerScheduledTask {

	@Autowired
	private SignUpService signUpService;
	
	private static final Logger LOG = LoggerFactory.getLogger(PasswordResetScavengerScheduledTask.class);
	
	@Scheduled(fixedRate = 300000)  // every 5 minutes
	public void scavengeUnusedPasswordResetRequests() {
		LOG.info("Scavenging password resets...");
		
		try {
			signUpService.removeExpiredPasswordResets();
		} catch (Throwable t) {
			LOG.error("Exception caught removing expired passwords", t);
		}
	}
}
