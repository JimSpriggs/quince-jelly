package uk.co.village_greens_coop.VillageGreensMemberPortal.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("uk.co.village_greens_coop.VillageGreensMemberPortal.aspect")
@EnableAspectJAutoProxy
public class AspectConfig {

}
