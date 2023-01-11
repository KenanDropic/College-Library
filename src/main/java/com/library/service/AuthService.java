package com.library.service;

import com.library.entity.ResetPasswordToken;
import com.library.entity.User;
import com.library.entity.VerificationToken;
import com.library.exception.exceptions.BadRequestException;
import com.library.exception.exceptions.NotFoundException;
import com.library.repository.UserRepository;
import com.library.security.UsernamePasswordAuthentication;
import com.library.security.UsernamePwdAuthenticateProvider;
import com.library.security.jwt.GenerateToken;
import com.library.utils.CustomCookie;
import com.library.utils.dto.Auth.CreateUserDto;
import com.library.utils.dto.Auth.LoginUserDto;
import com.library.utils.dto.Auth.ResetPasswordDto;
import com.library.utils.dto.Auth.ResetPasswordEmailDto;
import com.library.utils.payload.ResponseMessage;
import com.library.utils.projections.UserView;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@Transactional
@Slf4j
public class AuthService {
    private final UserService userService;
    private final EmailService emailService;
    private final VerificationTokenService vTokenService;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final GenerateToken generateToken;
    private final PasswordEncoder passwordEncoder;
    private final UsernamePwdAuthenticateProvider usernamePwdAuthenticateProvider;
    private final UserRepository userRepository;

    public AuthService(UserService userService, GenerateToken generateToken,
                       PasswordEncoder passwordEncoder, UserRepository userRepository,
                       UsernamePwdAuthenticateProvider usernamePwdAuthenticateProvider,
                       EmailService emailService, VerificationTokenService vTokenService,
                       ResetPasswordTokenService resetPasswordTokenService
    ) {
        this.userService = userService;
        this.generateToken = generateToken;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.usernamePwdAuthenticateProvider = usernamePwdAuthenticateProvider;
        this.emailService = emailService;
        this.vTokenService = vTokenService;
        this.resetPasswordTokenService = resetPasswordTokenService;
    }

    @SneakyThrows
    public ResponseEntity<ResponseMessage<ResponseBody>> signup(CreateUserDto params,
                                                                HttpServletResponse response) {
        if (emailExists(params.getEmail())) throw new BadRequestException("User already exists");

        User user = this.userService.createUser(params);

        String accessToken = generateToken.generateAToken(user);
        String refreshToken = generateToken.generateRToken(user);

        ResponseCookie cookie = CustomCookie.AssignCookie("jwt-rt", refreshToken);

        this.hashRT(user.getId(), refreshToken);
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        final String token = UUID.randomUUID().toString();
        vTokenService.createVerificationToken(user, token);
        constructLinkAndSendEmail(token, user);

        return ResponseEntity
                .status(201)
                .body(new ResponseMessage<>(true,
                        "You've registered  successfully. Please verify your email",
                        accessToken));
    }

    @SneakyThrows
    public ResponseEntity<ResponseMessage<ResponseBody>> signin(LoginUserDto params,
                                                                HttpServletResponse response) {
        User user = this.userService.findOneByEmail(params.getEmail());

        if (user == null) ResponseEntity
                .status(404)
                .body(new ResponseMessage<>(
                        true,
                        "User " + params.getEmail() + " not found!"));

        //noinspection unused
        Authentication auth = usernamePwdAuthenticateProvider
                .authenticate(new UsernamePasswordAuthentication(
                        params.getEmail(), params.getPassword()));

        String accessToken = generateToken.generateAToken(user);
        String refreshToken = generateToken.generateRToken(user);

        ResponseCookie cookie = CustomCookie.AssignCookie("jwt-rt", refreshToken);

        assert user != null;
        this.hashRT(user.getId(), refreshToken);
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(
                        true,
                        "You've logged in successfully",
                        accessToken));
    }

    public ResponseEntity<ResponseMessage<UserView>> getLoggedUser() {
        Authentication auth = getAuthContext();

        if ((auth instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity
                    .status(401)
                    .body(new ResponseMessage<>(
                            true,
                            "Failed to authenticate user"));
        }

        UserView user = this
                .userRepository
                .findUserByEmailWithRoles(auth.getPrincipal().toString());

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(true, user));
    }

    public ResponseEntity<ResponseMessage<ResponseBody>> refresh(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        Authentication auth = getAuthContext();

        Cookie cookieFromRequest = WebUtils.getCookie(request, "jwt-rt");

        if ((auth instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity
                    .status(401)
                    .body(new ResponseMessage<>(true, "Authentication failed!"));
        }

        User user = this.userRepository.findByEmail(auth.getPrincipal().toString());

        assert cookieFromRequest != null;
        boolean hashRTMatches = passwordEncoder.matches(cookieFromRequest.getValue(), user.getHashedRt());

        if (!hashRTMatches) return ResponseEntity
                .status(401)
                .body(new ResponseMessage<>(true, "Authentication failed!"));

        String accessToken = generateToken.generateAToken(user);
        String refreshToken = generateToken.generateRToken(user);

        ResponseCookie cookie = CustomCookie.AssignCookie("jwt-rt", refreshToken);
        this.hashRT(user.getId(), refreshToken);
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());


        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(
                        true,
                        "Token is refreshed successfully",
                        accessToken));
    }

    public ResponseEntity<ResponseMessage<ResponseBody>> logoutUser(HttpServletResponse response) {
        Authentication auth = getAuthContext();

        if ((auth instanceof AnonymousAuthenticationToken))
            return ResponseEntity
                    .status(401)
                    .body(new ResponseMessage<>(true, "Authentication failed!"));

        User user = this.userRepository.findByEmail(auth.getPrincipal().toString());

        if (user.getHashedRt().equals("") || user.getHashedRt() == null) {
            return ResponseEntity
                    .status(401)
                    .body(new ResponseMessage<>(true, "Hash is already empty"));
        }

        ClearAuth(response);
        user.setHashedRt(null);
        this.userRepository.save(user);

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(true, "Logout is successful"));
    }

    @Transactional
    public ResponseEntity<ResponseMessage<ResponseBody>> confirmEmail(String token) {
        VerificationToken verificationToken = vTokenService.getVerificationToken(token);

        if (verificationToken == null) {
            return ResponseEntity
                    .status(500)
                    .body(new ResponseMessage<>(false, "Invalid token"));
        }

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity
                    .status(500)
                    .body(new ResponseMessage<>(false, "Token expired"));
        }

        User user = verificationToken.getUser();
        if (user.getEmailConfirmed()) {
            return ResponseEntity
                    .status(400)
                    .body(new ResponseMessage<>(false, "Email is already confirmed"));
        }
        user.setEmailConfirmed(true);
        userRepository.save(user);

        // after email is confirmed,delete verification token
        this.vTokenService.deleteVerificationToken(user);

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(
                        true,
                        "Email confirmed successfully"));
    }

    @Transactional
    public ResponseEntity<ResponseMessage<ResponseBody>> resendConfirmEmailToken() {
        Authentication auth = getAuthContext();
        User user = this.userRepository.findByEmail(auth.getPrincipal().toString());

        if (user.getEmailConfirmed()) {
            return ResponseEntity
                    .status(400)
                    .body(new ResponseMessage<>(false, "Email is already confirmed"));
        }

        if (vTokenService.getVerificationTokenByUser(user) == null) {
            final String token = UUID.randomUUID().toString();
            vTokenService.createVerificationToken(user, token);
            constructLinkAndSendEmail(token, user);

            return ResponseEntity
                    .status(200)
                    .body(new ResponseMessage<>(true, "Email sent successfully!"));
        }

        String existingToken = vTokenService.getVerificationTokenByUser(user).getConfirmEmailToken();
        System.out.println("Existing token: " + existingToken);


        VerificationToken newToken = vTokenService.generateNewVerificationToken(user);
        constructLinkAndSendEmail(newToken.getConfirmEmailToken(), newToken.getUser());

        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(true, "Email sent successfully!"));
    }

    @Transactional
    public ResponseEntity<ResponseMessage<ResponseBody>> sendResetPasswordEmail(ResetPasswordEmailDto params) {
        User user = this.userRepository.findByEmail(params.getEmail());

        if (user == null) {
            return ResponseEntity
                    .status(404)
                    .body(new ResponseMessage<>(
                            false, "User " + params.getEmail() + " not found"));
        }

        ResetPasswordToken resetPasswordToken = resetPasswordTokenService.generateNewResetPasswordToken(user);
        constructResetPwdLinkAndSendEmail(resetPasswordToken.getResetPasswordToken(), user);
        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(true, "Email sent successfully"));
    }

    public ResponseEntity<ResponseMessage<ResponseBody>> changePassword(ResetPasswordDto params) {
        String result = resetPasswordTokenService.validatePasswordResetToken(params.getToken());
        if (result != null) {
            return ResponseEntity
                    .status(400)
                    .body(new ResponseMessage<>(false,
                            result.equals("Invalid token") ? "Reset password token is invalid"
                                    : "Reset password token expired"));
        }

        User user = this
                .resetPasswordTokenService
                .getResetPasswordToken(params.getToken()).getUser();

        if (user == null) {
            return ResponseEntity
                    .status(404)
                    .body(new ResponseMessage<>(true, "User not found"));
        }

        // comparing old user password with what he typed in field for old user password
        if (!resetPasswordTokenService.comparePasswords(user.getPassword(), params.getOldPassword())) {
            return ResponseEntity
                    .status(400)
                    .body(new ResponseMessage<>(true, "Please insert correct old password"));
        }

        this.userService.changeUserPassword(user, params.getNewPassword());
        return ResponseEntity
                .status(200)
                .body(new ResponseMessage<>(true, "Password changed successfully"));
    }

    /*------------------------------- NON-API -----------------------------------*/
    private void hashRT(Long id, String rt) {
        if (rt.isBlank() || id == null) throw new BadRequestException("Invalid params");

        String hash = this.passwordEncoder.encode(rt);
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        if (user != null) {
            user.setHashedRt(hash);
            this.userRepository.save(user);
        }
    }

    @NotNull
    public Authentication getAuthContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    private void constructLinkAndSendEmail(String verificationToken, User user) {
        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + verificationToken;
        emailService.send(user.getEmail(), emailService
                        .buildEmail(user.getFirstName(), link, "confirm email"),
                "Email Confirmation");
    }

    private void constructResetPwdLinkAndSendEmail(String resetPasswordToken, User user) {
        // http://localhost:8080/api/v1/auth/changePassword?token= + resetPasswordToken
        String link = "http://localhost:3000/resetPassword?token=" + resetPasswordToken;
        emailService.send(user.getEmail(), emailService
                        .buildEmail(user.getFirstName(), link, "reset password"),
                "Reset Password");
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    public static void ClearAuth(HttpServletResponse response) {
        ResponseCookie deletedCookie = CustomCookie.ClearCookie("jwt-rt");
        response.setHeader(HttpHeaders.SET_COOKIE, deletedCookie.toString());
        SecurityContextHolder.clearContext();
    }
}
