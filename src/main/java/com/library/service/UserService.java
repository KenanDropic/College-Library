package com.library.service;

import com.library.entity.User;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.RoleRepository;
import com.library.repository.UserRepository;
import com.library.service.interfaces.IUserService;
import com.library.utils.SortingPagination;
import com.library.utils.dto.Auth.CreateUserDto;
import com.library.utils.payload.PaginationResponse;
import com.library.utils.projections.UserLoansView;
import com.library.utils.projections.UserView;
import com.library.utils.projections.UsersView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ResetPasswordTokenService resetPasswordTokenService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       ResetPasswordTokenService resetPasswordTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.resetPasswordTokenService = resetPasswordTokenService;
    }

    public ResponseEntity<PaginationResponse> findAllWithPaginationAndSorting(int page, int pageSize,
                                                                              String field, String direction) {
        SortingPagination.containsDirection(direction);
        SortingPagination.containsField(List.of("first_name", "created_at"), field);

        Pageable paging = PageRequest.of(page - 1, pageSize);
        Page<UsersView> users = this.userRepository.findAllUsers(field, direction, paging);

        SortingPagination.doesHaveNext(users, page);

        if (users.isEmpty()) {
            throw new NotFoundException("Users not found");
        }

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, users.getSize(), users.getTotalElements(),
                        users.getTotalPages(), page, SortingPagination.getPagination(),
                        users.getContent()));
    }

    public User findOneByEmail(String email) {
        if (email == null) throw new NotFoundException("Email cannot be null!");

        return this.userRepository.findByEmail(email);
    }

    public UserView findOne(Long id) {
        return this.userRepository
                .findUserByIdWithRoles(id)
                .orElseThrow(() -> new NotFoundException("User " + id + " not found."));

    }

    public List<UserLoansView> findUserLoans(Long id) {
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User " + id + " not found."));

        return this.userRepository.findUserLoans(user.getId());
    }

    public User createUser(CreateUserDto params) {
        User user = new User(params.getFirst_name(),
                params.getLast_name(),
                params.getEmail(),
                passwordEncoder.encode(params.getPassword()),
                params.getPhone());

        user.addRole(this.roleRepository.findByName("ROLE_USER"));
        return this.userRepository.save(user);
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        // after changing user's password,delete his reset password token
        resetPasswordTokenService.deleteResetPasswordToken(user);
    }

}
