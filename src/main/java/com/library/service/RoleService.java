package com.library.service;

import com.library.entity.Role;
import com.library.entity.User;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.RoleRepository;
import com.library.repository.UserRepository;
import com.library.utils.dto.User.UserRoleDto;
import com.library.utils.payload.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository,
                       UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public List<Role> getUserRoles(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        return this.roleRepository.findByUsers(user);
    }

    public ResponseEntity<ResponseMessage<List<Role>>> updateUserRoles(Long id, UserRoleDto role) {
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with the given ID!"));


        if (role.getRole().equals("ROLE_ADMIN")
                || role.getRole().equals("ROLE_LIBRARIAN")
                || role.getRole().equals("ROLE_USER")) {
            user.addRole(this.roleRepository.findByName(role.getRole()));

        } else {
            return ResponseEntity.status(400).body(
                    new ResponseMessage<>(false, "Please add valid value for role field"));
        }

        return ResponseEntity.status(200)
                .body(new ResponseMessage<>(
                        true,
                        "Update is successful",
                        this.roleRepository.findByUsers(user)));
    }
}
