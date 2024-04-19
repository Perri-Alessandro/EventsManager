package perriAlessandro.EventsManager.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record NewEventDTO(@NotEmpty(message = "Il Titolo dell'evento è obblogatorio")
                          @Size(min = 5, max = 35, message = "Il titolo deve essere compreso tra i 5 e i 35 caratteri")
                          String title,
                          @NotEmpty(message = "La Descrizione è obbligatoria")
                          @Size(min = 20, max = 120, message = "Lo Descrizione dev'essere compresa tra i 20 e i 120 caratteri")
                          String description,
                          @NotNull(message = "La Data è obbligatoria")
                          LocalDate date,
                          @NotEmpty(message = "Il Luogo dell'evento è obbligatorio")
                          @Size(min = 5, max = 30, message = "Il Luogo dev'essere compresa tra i 5 e i 30 caratteri")
                          String place) {

}
