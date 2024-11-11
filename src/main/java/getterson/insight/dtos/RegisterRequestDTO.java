package getterson.insight.dtos;

import java.time.LocalDate;

public record RegisterRequestDTO(String name,
                                 String username,
                                 String document,
                                 String birthDate,
                                 String email,
                                 String password,
                                 String phone) {}
