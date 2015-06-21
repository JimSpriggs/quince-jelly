package uk.co.village_greens_coop.VillageGreensMemberPortal.config;

import javax.mail.Session;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
//@Profile("default")
public class JavaMailConfig implements MailConfig {

	private static final Logger LOG = LoggerFactory.getLogger(JavaMailConfig.class);
	
    @Bean
	@Override
	public MailSender mailSender() {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        JndiTemplate jndi = new JndiTemplate();
        Session mailSession = null;
        try {
            mailSession = (Session) jndi.lookup("java:comp/env/mail/vgMembersSmtp");
        } catch (NamingException e) {
            LOG.error("NamingException for java:comp/env/mail/vgMembersSmtp", e);
            return null;
        }

    	sender.setSession(mailSession);
    	
    	return sender;
    }
}
