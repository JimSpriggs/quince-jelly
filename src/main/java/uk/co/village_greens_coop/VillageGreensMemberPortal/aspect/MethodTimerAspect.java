package uk.co.village_greens_coop.VillageGreensMemberPortal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodTimerAspect {

	private static final Logger LOG = LoggerFactory.getLogger(MethodTimerAspect.class);

	@Pointcut("execution(* uk.co.village_greens_coop.VillageGreensMemberPortal.service.DocumentService+.*(..))"
			+ " || execution(* uk.co.village_greens_coop.VillageGreensMemberPortal.service.EmailService+.*(..))"
			+ " || execution(* uk.co.village_greens_coop.VillageGreensMemberPortal.web.AdminController+.*(..))")
	public void profilableMethod() { }

    @Around("profilableMethod()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        LOG.debug("WAYMARK - about to call method " + pjp.toString());
        Object output = pjp.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        LOG.debug(String.format("WAYMARK - completed call of method %s in %dms", pjp.toString(), elapsedTime));
        return output;
    }
}
