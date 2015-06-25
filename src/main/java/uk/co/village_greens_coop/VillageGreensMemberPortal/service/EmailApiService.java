package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.StockEmailRows;

@Service
public class EmailApiService {

	@Autowired
	private EmailService emailService;
	
	public StockEmailRows getStockEmails() {
		return new StockEmailRows(emailService.getAllStockEmailRows());
	}
}
