package perriAlessandro.EventsManager.payloads;

import jakarta.validation.constraints.*;

public record NewUserDTO(
        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è valida")
        String email,
        @NotNull(message = "L'username è obblogatorio")
        String username,
        @NotEmpty(message = "La password è obbligatoria")
        @Size(min = 4, message = "La password deve avere come minimo 8 caratteri")
        @Pattern.List({
                @Pattern(regexp = "^(?=.*[0-9]).+$", message = "La password deve contenere almeno un numero"),
                @Pattern(regexp = "^(?=.*[A-Z]).+$", message = "La password deve contenere almeno una lettera maiuscola")
        })
        String password) {
}
