package perriAlessandro.EventsManager.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import perriAlessandro.EventsManager.entities.Event;
import perriAlessandro.EventsManager.exceptions.NotFoundException;
import perriAlessandro.EventsManager.payloads.NewEventDTO;
import perriAlessandro.EventsManager.repositories.EventDAO;

import java.io.IOException;

@Service
public class EventService {
    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private Cloudinary cloudinaryUploader;


    public Page<Event> getEventList(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventDAO.findAll(pageable);
    }

    public Event saveEvent(NewEventDTO body) {
        Event newEvent = new Event();
        newEvent.setTitle(body.getTitle());
        newEvent.setDescription(body.getDescription());
        newEvent.setDate(body.getDate());
        newEvent.setPlace(body.getPlace());
        return eventDAO.save(newEvent);
    }

    public void updateEvent(Event evento) {
        eventDAO.save(evento);
    }

    public Event findById(Long id) {
        return this.eventDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Event findByIdAndUpdate(Long id, NewEventDTO updatedEvent) {
        Event found = this.findById(id);
        found.setTitle(updatedEvent.getTitle());
        found.setDescription(updatedEvent.getDescription());
        found.setDate(updatedEvent.getDate());
        found.setPlace(updatedEvent.getPlace());
        return eventDAO.save(found);
    }


    public void findByIdAndDelete(Long id) {
        Event found = this.findById(id);
        this.eventDAO.delete(found);
    }

    public String updateImage(Long eventId, MultipartFile image) throws IOException {
        Event event = eventDAO.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
        String newImageUrl = (String) cloudinaryUploader.uploader().upload(image.getBytes(), ObjectUtils.emptyMap()).get("url");
        event.setImage(newImageUrl);
        eventDAO.save(event);
        return newImageUrl;
    }
}
