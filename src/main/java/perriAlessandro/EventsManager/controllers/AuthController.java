package perriAlessandro.EventsManager.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import perriAlessandro.EventsManager.exceptions.BadRequestException;
import perriAlessandro.EventsManager.payloads.NewUserDTO;
import perriAlessandro.EventsManager.payloads.NewUserRespDTO;
import perriAlessandro.EventsManager.payloads.UserLoginDTO;
import perriAlessandro.EventsManager.payloads.UserLoginResponseDTO;
import perriAlessandro.EventsManager.services.AuthService;
import perriAlessandro.EventsManager.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO body) {
        return new UserLoginResponseDTO(this.authService.authenticateUserAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO saveUser(@RequestBody @Validated NewUserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return new NewUserRespDTO(this.userService.saveUser(body).getId());
    }
}
