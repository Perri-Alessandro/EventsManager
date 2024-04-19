package perriAlessandro.EventsManager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import perriAlessandro.EventsManager.entities.User;
import perriAlessandro.EventsManager.exceptions.BadRequestException;
import perriAlessandro.EventsManager.exceptions.UnauthorizedException;
import perriAlessandro.EventsManager.payloads.UserLoginDTO;
import perriAlessandro.EventsManager.security.JWTTools;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUserAndGenerateToken(UserLoginDTO payload) {
        if (payload.email() == null || payload.password() == null) {
            throw new BadRequestException("Email e/o password mancanti nel payload di autenticazione.");
        }
        User user = this.userService.findByEmail(payload.email());
        if (bcrypt.matches(payload.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali non valide! Effettua di nuovo il login!");
        }

    }
}
