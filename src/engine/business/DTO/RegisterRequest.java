package engine.business.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//@Pattern(regexp = ".+\\.\\w+")
public record RegisterRequest(@Email(regexp = ".+\\.\\w+")  @NotBlank String email
        , @Size(min = 5) @NotBlank String password) {}
