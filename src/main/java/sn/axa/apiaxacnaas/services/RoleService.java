package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.entities.Role;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.repositories.RoleRepository;
import sn.axa.apiaxacnaas.util.RoleEnum;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role CreateRoleIfNotExist(RoleEnum name){
        return roleRepository.findByName(name).orElseGet(()->roleRepository.save(Role.builder().name(name).build()));
    }
}
