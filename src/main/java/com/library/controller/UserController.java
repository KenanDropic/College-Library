package com.library.controller;

import com.library.entity.User;
import com.library.service.RoleService;
import com.library.service.UserService;
import com.library.utils.dto.Auth.CreateUserDto;
import com.library.utils.dto.User.UserRoleDto;
import com.library.utils.payload.PaginationResponse;
import com.library.utils.projections.UserLoansView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService,
                          RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse> getUsers(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                       @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                       @RequestParam(required = false, defaultValue = "first_name") String field,
                                                       @RequestParam(required = false, defaultValue = "ASC") String direction) {
        return this.userService.findAllUsersWithPaginationAndSorting(page, pageSize, field, direction);
    }

    @GetMapping(path = "/{id}")
    public UserLoansView getUser(@PathVariable("id") final Long userId) {
        return this.userService.findUserWithRolesAndLoans(userId);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid CreateUserDto userParams) {
        return this.userService.createUser(userParams);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserRoles(@PathVariable("id") Long userId,
                                             @RequestBody @Valid UserRoleDto role) {
        return this.roleService.updateUserRoles(userId, role);
    }

}
