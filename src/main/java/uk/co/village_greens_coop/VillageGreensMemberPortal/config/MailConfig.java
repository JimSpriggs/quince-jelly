package uk.co.village_greens_coop.VillageGreensMemberPortal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

@Configuration
public interface MailConfig {
	MailSender mailSender();
}
