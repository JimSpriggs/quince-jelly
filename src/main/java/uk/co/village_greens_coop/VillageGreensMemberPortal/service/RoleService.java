package uk.co.village_greens_coop.VillageGreensMemberPortal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.village_greens_coop.VillageGreensMemberPortal.dao.RoleDao;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.Role;

@Service
public class RoleService {

	@Autowired
	private RoleDao roleRepository;
	
	public List<Role> getAll() {
		List<Role> rolesList = roleRepository.getAll();
		
		return rolesList;
	}

	public Role getRole(String name) {
		return roleRepository.findByName(name);
	}
}
