package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
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

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.AccountDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.RoleDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Account;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Role;

@Service
public class UserService implements UserDetailsService {

	Logger logger = Logger.getLogger(UserService.class);
	
	@Autowired
	private AccountDao accountRepository;
	
	@Autowired
	private RoleDao roleRepository;

	@PostConstruct	
	protected void initialize() {
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, LockedException {
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
		return new User(account.getEmail(), account.getPassword(), true, true, true, account.isActive(), createAuthorities(account));
	}

	private Set<GrantedAuthority> createAuthorities(Account account) {
		Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
		for (Role role : account.getRoles()) {
			auths.add(new SimpleGrantedAuthority(role.getName()));
		}
		return auths;
	}

}
