package perriAlessandro.EventsManager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import perriAlessandro.EventsManager.entities.Event;
import perriAlessandro.EventsManager.entities.User;
import perriAlessandro.EventsManager.exceptions.BadRequestException;
import perriAlessandro.EventsManager.exceptions.NotFoundException;
import perriAlessandro.EventsManager.payloads.NewUserDTO;
import perriAlessandro.EventsManager.repositories.EventDAO;
import perriAlessandro.EventsManager.repositories.UserDAO;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private PasswordEncoder bcrypt;
    
    public Page<User> getUsersList(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.userDAO.findAll(pageable);
    }

    public User saveUser(NewUserDTO body) {
        if (userDAO.findByEmail(body.email()).isPresent()) {
            throw new BadRequestException("L'email " + body.email() + " è già in uso!");
        }
        User newUser = new User(body.username(), bcrypt.encode(body.password()), body.email());

        return userDAO.save(newUser);
    }

    public User findById(Long id) {
        return this.userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByIdAndUpdate(long id, User updatedUser) {
        User found = this.findById(id);
        found.setUsername(updatedUser.getUsername());
        found.setEmail(updatedUser.getEmail());
        found.setPassword(updatedUser.getPassword());
        return userDAO.save(found);
    }

    public void findByIdAndDelete(Long id) {
        User found = this.findById(id);
        userDAO.delete(found);
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con mail " + email + " non trovato."));
    }

    public void addReservation(Long userId, Long eventId) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        Event event = eventDAO.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));

        user.getEvents().add(event);
        event.getParticipants().add(user);

        userDAO.save(user);
        eventDAO.save(event);
    }

    public void removeReservation(Long userId, Long eventId) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        Event event = eventDAO.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));

        user.getEvents().remove(event);
        event.getParticipants().remove(user);

        userDAO.save(user);
        eventDAO.save(event);
    }

}
