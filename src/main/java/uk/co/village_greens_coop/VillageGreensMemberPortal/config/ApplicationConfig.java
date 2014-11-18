package uk.co.village_greens_coop.VillageGreensMemberPortal.config;import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.ComponentScan;import org.springframework.context.annotation.ComponentScan.Filter;import org.springframework.context.annotation.Configuration;import org.springframework.core.io.ClassPathResource;import org.springframework.core.io.Resource;import org.springframework.stereotype.Controller;import uk.co.village_greens_coop.VillageGreensMemberPortal.Application;@Configuration@ComponentScan(basePackageClasses = Application.class, excludeFilters = @Filter({Controller.class, Configuration.class}))class ApplicationConfig {		@Bean	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();		ppc.setLocations(new Resource[] {			new ClassPathResource("persistence.properties"),			new ClassPathResource("mail.properties")		});		return ppc;	}}