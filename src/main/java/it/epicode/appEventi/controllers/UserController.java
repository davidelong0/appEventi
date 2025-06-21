package it.epicode.appEventi.controllers;

import it.epicode.appEventi.dto.EventDTO;
import it.epicode.appEventi.dto.UserDTO;
import it.epicode.appEventi.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserController {
    private final EventService eventService;

    @GetMapping("/bookings")
    public List<EventDTO> getMyBookings() {
        return eventService.getMyBookings();
    }

    @GetMapping("/events")
    public List<EventDTO> getMyCreatedEvents() {
        return eventService.getMyCreatedEvents();
    }

    @GetMapping
    public UserDTO getMyProfile() {
        return eventService.getCurrentUserDTO();
    }
}


