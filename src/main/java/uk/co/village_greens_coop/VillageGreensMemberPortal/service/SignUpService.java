package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.AccountDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.RoleDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Account;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Role;

@Service
@Transactional
public class SignUpService {

	@Autowired
	private AccountDao accountRepository;
	
	@Autowired
	private RoleDao roleRepository;

	public void signup(String firstName, String surname, String email, String password) {
		// new users default to the basic ROLE_USER role
		Role role = roleRepository.findByName("ROLE_USER");
		Account account = new Account(firstName, surname, email, password, role);
		accountRepository.save(account);
	}
}
