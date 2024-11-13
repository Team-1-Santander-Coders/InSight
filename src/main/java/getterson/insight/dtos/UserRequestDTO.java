package getterson.insight.dtos;

import java.time.LocalDate;

public record UserRequestDTO(String name,
                             String username,
                             String document,
                             LocalDate birthDate,
                             String email,
                             String password,
                             String phone) {}
