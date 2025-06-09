package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import junit.framework.TestCase;
import org.junit.Test;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Member;

import java.math.BigDecimal;
import java.util.Date;

public class CertificateServiceTest extends TestCase {

    private final CertificateService underTest = new CertificateService();

    @Test
    public void testGenerateMemberCertificate() {
        Member member = new Member("Mr", "Jim", "Spriggs", "jim@spriggs.com",
                                    "Address Line 1", "Address Line 2", "Address Line 3", "Address Line 4", "W1A 4WW",
                                    "18/07/1970", new BigDecimal("500.00"), false, false);
        member.setMemberno(123L);
        member.setCertificateGenerated(new Date());
        underTest.generateMemberCertificate(member);
    }
}