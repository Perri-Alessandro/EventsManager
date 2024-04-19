package perriAlessandro.EventsManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import perriAlessandro.EventsManager.entities.Event;
import perriAlessandro.EventsManager.entities.User;
import perriAlessandro.EventsManager.exceptions.BadRequestException;
import perriAlessandro.EventsManager.exceptions.UnauthorizedException;
import perriAlessandro.EventsManager.payloads.NewEventDTO;
import perriAlessandro.EventsManager.payloads.NewEventRespDTO;
import perriAlessandro.EventsManager.services.EventService;

import java.io.IOException;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping
    public Page<Event> getEventList(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        return eventService.getEventList(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewEventRespDTO saveEvent(@RequestBody @Validated NewEventDTO body, BindingResult validation, @AuthenticationPrincipal User currentUser) {
        if (!currentUser.getRole().equals("Admin")) {
            throw new UnauthorizedException("Solo gli amministratori possono creare eventi.");
        }

        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return new NewEventRespDTO(this.eventService.saveEvent(body).getId());
    }

    @GetMapping("/{eventId}")
    public Event findEventById(@PathVariable Long eventId) {
        return eventService.findById(eventId);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('Admin')")
    public Event findDeviceByIdAndUpdate(@PathVariable Long eventId, @RequestBody NewEventDTO body) {
        return eventService.findByIdAndUpdate(eventId, body);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('Admin')")
    public void findByEventIdAndDelete(@PathVariable Long eventId) {
        eventService.findByIdAndDelete(eventId);
    }

    @PatchMapping("/{eventId}/update")
    @PreAuthorize("hasRole('Admin')")
    public String updateImage(@PathVariable Long eventId, @RequestParam("image") MultipartFile image) throws IOException {
        return this.eventService.updateImage(eventId, image);
    }
}
