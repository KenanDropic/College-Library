package com.library.controller;

import com.library.service.AuthService;
import com.library.utils.dto.Auth.CreateUserDto;
import com.library.utils.dto.Auth.LoginUserDto;
import com.library.utils.dto.Auth.ResetPasswordDto;
import com.library.utils.dto.Auth.ResetPasswordEmailDto;
import com.library.utils.payload.ResponseMessage;
import com.library.utils.projections.UserView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage<ResponseBody>> register(@RequestBody @Valid CreateUserDto params,
                                                                  HttpServletResponse response) {
        return this.authService.signup(params, response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<ResponseBody>> login(@RequestBody @Valid LoginUserDto params,
                                                               HttpServletResponse response) {
        return this.authService.signin(params, response);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseMessage<UserView>> getCurrentUser() {
        return this.authService.getLoggedUser();
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage<ResponseBody>> logout(HttpServletResponse response) {
        return this.authService.logoutUser(response);
    }

    @GetMapping("/refresh")
    public ResponseEntity<ResponseMessage<ResponseBody>> refreshToken(HttpServletRequest request,
                                                                      HttpServletResponse response) {
        return this.authService.refresh(request, response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<ResponseMessage<ResponseBody>> confirmEmail(@RequestParam("token") String token) {
        return this.authService.confirmEmail(token);
    }

    @GetMapping("/resendConfirmToken")
    public ResponseEntity<ResponseMessage<ResponseBody>> resendConfirmToken() {
        return this.authService.resendConfirmEmailToken();
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseMessage<ResponseBody>> resetPassword(@RequestBody @Valid ResetPasswordEmailDto params) {
        return this.authService.sendResetPasswordEmail(params);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<ResponseMessage<ResponseBody>> changePassword(@RequestBody @Valid ResetPasswordDto params) {
        return this.authService.changePassword(params);
    }
}
