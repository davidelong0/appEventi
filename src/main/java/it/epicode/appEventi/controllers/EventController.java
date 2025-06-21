package it.epicode.appEventi.controllers;

import it.epicode.appEventi.dto.EventDTO;
import it.epicode.appEventi.models.Event;
import it.epicode.appEventi.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping
    public EventDTO createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PutMapping("/{id}")
    public EventDTO updateEvent(@PathVariable Long id, @RequestBody Event updated) {
        return eventService.updateEvent(id, updated);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    @PostMapping("/{id}/book")
    public EventDTO bookEvent(@PathVariable Long id) {
        return eventService.bookEvent(id);
    }

    @PostMapping("/{id}/cancel")
    public EventDTO cancelBooking(@PathVariable Long id) {
        return eventService.cancelBooking(id);
    }
}

