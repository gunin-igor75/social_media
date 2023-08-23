package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.entity.user.Role;
import com.github.guninigor75.social_media.repository.RoleRepository;
import com.github.guninigor75.social_media.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String name) {
        Optional<Role> roleOrEmpty = roleRepository.findByName(name);
        if (roleOrEmpty.isPresent()) {
            return roleOrEmpty.get();
        }
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }
}
