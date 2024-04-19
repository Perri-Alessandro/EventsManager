package perriAlessandro.EventsManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import perriAlessandro.EventsManager.entities.Event;
import perriAlessandro.EventsManager.entities.User;

import java.util.List;

public interface EventDAO extends JpaRepository<Event, Long> {
    boolean existsByUser(User user);

    List<Event> findByUser(User user);
}
