package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.AccountDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Account;

@Service
public class AccountService {

	@Autowired
	private AccountDao accountRepository;
	
	public Account getAccountByEmailAddress(String email) {
		Account account = accountRepository.findByEmail(email);
		return account; 
	}

}
