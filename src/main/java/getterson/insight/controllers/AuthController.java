package getterson.insight.controllers;

import getterson.insight.entities.UserEntity;
import getterson.insight.exceptions.user.*;
import getterson.insight.services.JWTService;
import getterson.insight.services.UserService;
import getterson.insight.utils.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import getterson.insight.exceptions.AuthenticationFailedException;
import getterson.insight.dtos.LoginRequestDTO;
import getterson.insight.dtos.RegisterRequestDTO;
import getterson.insight.dtos.ResponseDTO;

import static getterson.insight.utils.DateUtil.stringToDate;
import static getterson.insight.utils.DateUtil.DEFAULT_DATE_PATTERN;

import java.util.Optional;

@RestController
public class AuthController {
    private final UserService userService;
    private final JWTService jwtService;

    public AuthController(UserService userService, JWTService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Optional<UserEntity> user = userService.validateUserLogin(loginRequestDTO.email(), loginRequestDTO.password());
            if (user.isEmpty()) throw new AuthenticationFailedException("Usuário ou senha incorretos");
            else return ResponseEntity.ok(new ResponseDTO(this.jwtService.generateToken(user.get()), user.get().getUsername()));
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            UserEntity user = userService.registerUser(registerRequestDTO.name(), registerRequestDTO.username(), registerRequestDTO.document(), stringToDate(registerRequestDTO.birthDate(), DEFAULT_DATE_PATTERN), registerRequestDTO.email(), registerRequestDTO.password(), registerRequestDTO.phone());
            return ResponseEntity.ok(new ResponseDTO(this.jwtService.generateToken(user), user.getUsername()));
        } catch (InvalidEmailException | InvalidDocumentException |
                 AuthenticationFailedException | InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicatedUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
