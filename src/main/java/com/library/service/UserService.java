package com.library.service;

import com.library.entity.User;
import com.library.exception.exceptions.BadRequestException;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.RoleRepository;
import com.library.repository.UserRepository;
import com.library.service.interfaces.IUserService;
import com.library.utils.SortingPagination;
import com.library.utils.dto.Auth.CreateUserDto;
import com.library.utils.dto.User.UserDto;
import com.library.utils.payload.PaginationResponse;
import com.library.utils.projections.UserLoansView;
import com.library.utils.projections.UsersView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
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
        // subtracting one from page,since in pagination it starts from 0,but in frontend we will send values from 1
        List<String> fields = Arrays.asList("first_name", "created_at");
        List<String> directions = Arrays.asList("ASC", "DESC");

        if (!fields.contains(field)) {
            throw new BadRequestException("Sorting is allowed only by two fields: first_name and created_at");
        }

        if (!directions.contains(direction)) {
            throw new BadRequestException("Sorting is possible only by two directions: ASC or DESC");
        }

        Pageable paging = PageRequest.of(page - 1, pageSize);
        Page<UsersView> users = this.userRepository.findAllUsers(field, direction, paging);

        SortingPagination pagination = new SortingPagination();
        pagination.doesHaveNext(users, page);

        if (users.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new PaginationResponse(true, 0, users.getTotalPages(),
                            page, users.getContent()));
        }

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, users.getSize(), users.getTotalElements(),
                        users.getTotalPages(), page, pagination.getPagination(), users.getContent()));
    }


    public User findOneByEmail(String email) {
        if (email == null) throw new NotFoundException("User not found!");

        return this.userRepository.findByEmail(email);
    }

    public UserDto findOne(Long id) {
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        List<String> userRoles = this.userRepository.findUserRoles(user.getId());

        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getEmailConfirmed(), userRoles, user.getPhone(),
                user.getCreatedAt(), user.getUpdatedAt());

    }

    public List<UserLoansView> findUserLoans(Long id) {
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with the given ID!"));

        return this.userRepository.findUserLoans(user.getId());
    }


    public List<String> findUserRoles(Long id) {
        return this.userRepository.findUserRoles(id);
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
