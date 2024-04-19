package perriAlessandro.EventsManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import perriAlessandro.EventsManager.entities.User;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
