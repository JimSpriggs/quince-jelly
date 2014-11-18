package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.DashboardDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Dashboard;

@Service
public class DashboardService {

	@Autowired
	private DashboardDao dashboardRepository;
	
	public Dashboard getDashboardFigures() {
		Dashboard dashboard = new Dashboard();
		dashboardRepository.getMemberCountsAndPayments(dashboard);
		dashboard.setAllMembers(
					dashboard.getFullMembers() +
					dashboard.getPartMembers() +
					dashboard.getUnpaidMembers());
		dashboardRepository.getOverduePaymentsCount(dashboard);
		dashboardRepository.getTotalPledges(dashboard);
		dashboardRepository.getOverdueMembersCount(dashboard);
		dashboardRepository.getCertifiableMembersCount(dashboard);
		dashboardRepository.getStockEmailCount(dashboard);
		dashboardRepository.getDocumentCount(dashboard);
		return dashboard;
	}
}
