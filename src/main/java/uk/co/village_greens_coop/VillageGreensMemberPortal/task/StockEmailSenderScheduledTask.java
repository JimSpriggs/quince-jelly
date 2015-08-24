package uk.co.village_greens_coop.VillageGreensMemberPortal.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.co.village_greens_coop.VillageGreensMemberPortal.service.EmailService;

@Component
public class StockEmailSenderScheduledTask {

	@Autowired
	private EmailService emailService;
	
	private static final Logger LOG = LoggerFactory.getLogger(StockEmailSenderScheduledTask.class);
	
	@Scheduled(fixedRate = 300000)  // every 5 minutes
	public void scavengeUnusedPasswordResetRequests() {
		LOG.info("Sending member emails...");
		
		try {
			int numSent = emailService.sendQueuedStockEmails();
			LOG.info("Emails sent this run: {}", numSent);
		} catch (Throwable t) {
			LOG.error("Exception caught sending queued stock emails", t);
		}
	}
}
