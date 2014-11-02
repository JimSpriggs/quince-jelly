package uk.co.village_greens_coop.VillageGreensMemberPortal.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
//@Profile("default")
public class JavaMailConfig implements MailConfig {

    @Value("${mail.host}")
	private String host;
    @Value("${mail.port}")
	private int port;
    @Value("${mail.transport.protocol}")
	private String protocol;
    @Value("${mail.username}")
	private String username;
    @Value("${mail.password}")
	private String password;
	
    @Bean
	@Override
	public MailSender mailSender() {
    	JavaMailSenderImpl sender = new JavaMailSenderImpl();
    	sender.setHost(host);
    	sender.setPort(port);
    	sender.setProtocol(protocol);
    	sender.setUsername(username);
    	sender.setPassword(password);
    	
    	Properties mailProps = new Properties();
    	Resource mailPropsResource = new ClassPathResource("mail.properties");
    	try {
	    	mailProps.load(mailPropsResource.getInputStream());
	    	sender.setJavaMailProperties(mailProps);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return sender;
    }
}
