package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.AccountDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.RoleDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Account;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Role;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private AccountDao accountRepository;
	
	@Autowired
	private RoleDao roleRepository;

	@PostConstruct	
	protected void initialize() {
//		accountRepository.save(new Account("user", "demo", "ROLE_USER"));
//		accountRepository.save(new Account("admin", "admin", "ROLE_ADMIN"));
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(username);
		if(account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(account);
	}

	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, createAuthorities(account));		
	}
	
	private User createUser(Account account) {
		return new User(account.getEmail(), account.getPassword(), createAuthorities(account));
	}

	private Set<GrantedAuthority> createAuthorities(Account account) {
		Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
		for (Role role : account.getRoles()) {
			auths.add(new SimpleGrantedAuthority(role.getName()));
		}
		return auths;
	}

}
