package perriAlessandro.EventsManager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import perriAlessandro.EventsManager.entities.Event;

@Service
public class EventService {
    @Autowired
    private EventRepository eventoRepository;

    public Event createEvent(Event event) {
        return eventoRepository.save(event);
    }

    public void updateEvent(Event evento) {
        eventoRepository.save(evento);
    }

    public void deleteEvent(Long id) {
        eventoRepository.deleteById(id);
    }
}
