package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.AccountActivationDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.AccountDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.PasswordResetDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.RoleDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.email.EmailDetail;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Account;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.AccountActivation;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.PasswordReset;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Role;
import uk.co.village_greens_coop.VillageGreensMemberPortal.signup.PasswordResetForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.utils.RequestUtils;

@Service
@Transactional
public class SignUpService {

	private static Logger LOG = LoggerFactory.getLogger(SignUpService.class);
	
	@Autowired
	private AccountDao accountRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AccountActivationDao accountActivationRepository;

	@Autowired
	private PasswordResetDao passwordResetRepository;

	@Autowired
	private RoleDao roleRepository;
	
	@Autowired
	private EmailService emailService;

	public void signup(HttpServletRequest request, String firstName, String surname, String email, String password) {
		// new users default to the basic ROLE_USER role
		Role role = roleRepository.findByName("ROLE_USER");
		Account account = new Account(firstName, surname, email, password, role);
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		AccountActivation accountActivation = new AccountActivation(account);
		accountRepository.save(account);
		accountActivationRepository.save(accountActivation);
		
		String emailBody = 
				"Hi " + account.getFirstName() + "\n\n"
				+ "Thank you for signing up for an account at Village Greens Coop.\n\n"
				+ "To activate your account simply click on the link below, then you can log in using your email address and the password you provided previously.\n"
				+ "\n"
				+ RequestUtils.getAppURL(request) + "/activate?id=" + account.getId() + "&key=" + accountActivation.getAccessKey() + "\n"
				+ "\n"
				+ "Thanks!\n\n\n"
				+ "The Village Greens technical team\n";
							
		EmailDetail emailDetail = new EmailDetail(
									account.getEmail(), 
									"info@village-greens-coop.co.uk", 
									"Village Greens Info",
									"Your Village Greens Account needs to be activated",
									emailBody);

		LOG.info("Sending account activation email via emailService for account {}", account.getId());
		emailService.sendEmail(emailDetail);
	}
	
	public boolean activate(Long id, String key) {
		AccountActivation activation = accountActivationRepository.findByAccessKey(key);
		if (activation != null) {
			Account account = activation.getAccount();
			if (account != null) {
				if (account.getId().equals(id)) {
					// activate the account
					if (account.isActive()) {
						// no need to activate, the account is already active
						return false;
					}
					
					// set the activation details on the account
					account.setActivationDate(new Date());
					account.setActive(true);
					accountRepository.save(account);
					accountActivationRepository.delete(activation);
					return true;
				}
			}
		}
		return false;
	}
	
	public void forgottenPassword(HttpServletRequest request, String email) {
		Account account = accountRepository.findByEmail(email);
		if (account != null) {
			PasswordReset passwordReset = new PasswordReset(account);
			passwordResetRepository.save(passwordReset);

			String emailBody = 
					"Hi " + account.getFirstName() + "\n\n"
					+ "Village Greens Coop has received a request to reset the password for the account with this email address.\n\n"
					+ "If you did not make this request, then please kindly ignore and/or delete this email.\n"
					+ "\n"
					+ "However, to continue with the password reset process, please click the link below, then follow the instructions:\n"
					+ "\n"
					+ RequestUtils.getAppURL(request) + "/resetPassword?id=" + account.getId() + "&key=" + passwordReset.getAccessKey() + "\n"
					+ "\n"
					+ "Thanks!\n\n\n"
					+ "The Village Greens Technical Team\n";
								
			EmailDetail emailDetail = new EmailDetail(
										account.getEmail(), 
										"info@village-greens-coop.co.uk", 
										"Village Greens Info",
										"Your Village Greens password",
										emailBody);

			LOG.info("Sending password reset email via emailService for account {}", account.getId());
			emailService.sendEmail(emailDetail);
		}
	}

	public PasswordResetForm getAccountDetailsForPasswordReset(Long id, String key) {
		PasswordReset passwordReset = passwordResetRepository.findByAccessKey(key);
		if (passwordReset != null) {
			Account account = passwordReset.getAccount();
			if (account != null && account.getId().equals(id)) {
				PasswordResetForm passwordResetForm = new PasswordResetForm();
				passwordResetForm.setFirstName(account.getFirstName());
				passwordResetForm.setSurname(account.getSurname());
				passwordResetForm.setEmail(account.getEmail());
				passwordResetForm.setPassword("");;
				passwordResetForm.setRepeatPassword("");
				passwordResetForm.setId(id);
				passwordResetForm.setKey(key);
				return passwordResetForm;
			}
		}
		return null;
	}
	
	public boolean updateViaPasswordReset(HttpServletRequest request, Long accountId, String key, String password) {
		// new users default to the basic ROLE_USER role
		PasswordReset passwordReset = passwordResetRepository.findByAccessKey(key);
		
		if (passwordReset != null && passwordReset.getAccount().getId().equals(accountId)) {
			// ok, the details tally with those of a genuine password reset request
			Account account = accountRepository.findById(accountId);
			account.setPassword(passwordEncoder.encode(password));
			accountRepository.save(account);
			return true;
		}
		return false;
	}
	
	public void removeExpiredPasswordResets() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -30);
		Date cutOffDate = cal.getTime();

		LOG.info("Expiring any password reset request created prior to {}", cutOffDate);
		List<PasswordReset> passwordResets = passwordResetRepository.findAllExpired(cutOffDate);
		if (passwordResets != null && passwordResets.size() > 1) {
			for (PasswordReset passwordReset : passwordResets) {
				LOG.info("Removing expired password reset {} - {}", passwordReset.getId(), passwordReset.getAccessKey());
				passwordResetRepository.delete(passwordReset);
			}
		} else {
			LOG.info("No expired password resets found");
		}
		
	}
}
