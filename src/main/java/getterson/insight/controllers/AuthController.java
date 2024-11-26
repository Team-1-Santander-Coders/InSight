package getterson.insight.controllers;

import getterson.insight.entities.UserEntity;
import getterson.insight.exceptions.user.*;
import getterson.insight.services.JWTService;
import getterson.insight.services.UserService;
import getterson.insight.utils.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.OPTIONS, path = "/**")
    public void handleOptionsRequest(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    @Operation(summary = "Login de usuário", description = "Realiza o login do usuário, retornando um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Optional<UserEntity> user = userService.validateUserLogin(loginRequestDTO.login(), loginRequestDTO.password());
            if (user.isEmpty()) throw new AuthenticationFailedException("Usuário ou senha incorretos");
            else {
                return ResponseEntity.ok(new ResponseDTO(this.jwtService.generateToken(user.get()), user.get().getUsername()));
            }
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "Registro de usuário", description = "Realiza o registro de um novo usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe")
    })

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            UserEntity user = userService.registerUser(registerRequestDTO.name(), registerRequestDTO.username(), registerRequestDTO.document(), stringToDate(registerRequestDTO.birthDate(), DEFAULT_DATE_PATTERN), registerRequestDTO.email(), registerRequestDTO.password(), registerRequestDTO.phone());
            return ResponseEntity.ok(new ResponseDTO(this.jwtService.generateToken(user), user.getUsername()));
        } catch (InvalidEmailException | InvalidDocumentException |
                 AuthenticationFailedException | InvalidPasswordException | InvalidPhoneException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicatedUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
