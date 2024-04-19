package perriAlessandro.EventsManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import perriAlessandro.EventsManager.entities.User;
import perriAlessandro.EventsManager.exceptions.BadRequestException;
import perriAlessandro.EventsManager.payloads.NewUserDTO;
import perriAlessandro.EventsManager.payloads.NewUserRespDTO;
import perriAlessandro.EventsManager.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public User updateProfile(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody User updatedUser) {
        return this.userService.findByIdAndUpdate(currentAuthenticatedUser.getId(), updatedUser);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        this.userService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO saveUser(@RequestBody @Validated NewUserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return new NewUserRespDTO(this.userService.saveUser(body).getId());
    }

    @PatchMapping("/{userId}/events/{eventId}/reserve")
    public ResponseEntity<String> addReservation(@PathVariable Long userId, @PathVariable Long eventId) {
        userService.addReservation(userId, eventId);
        return ResponseEntity.ok("Prenotazione aggiunta con successo");
    }

    @PatchMapping("/{userId}/events/{eventId}/cancel")
    public ResponseEntity<String> removeReservation(@PathVariable Long userId, @PathVariable Long eventId) {
        userService.removeReservation(userId, eventId);
        return ResponseEntity.ok("Prenotazione rimossa con successo");
    }
}
