package it.epicode.appEventi.services;

import it.epicode.appEventi.dto.EventDTO;
import it.epicode.appEventi.dto.UserDTO;
import it.epicode.appEventi.exceptions.NotFoundException;
import it.epicode.appEventi.models.Event;
import it.epicode.appEventi.models.User;
import it.epicode.appEventi.models.enums.Role;
import it.epicode.appEventi.repositories.EventRepository;
import it.epicode.appEventi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepo;
    private final UserRepository userRepo;

    public EventDTO createEvent(Event event) {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != Role.ORGANIZER) {
            throw new IllegalStateException("Solo gli organizzatori possono creare eventi");
        }

        event.setCreator(currentUser);
        return toDTO(eventRepo.save(event));
    }

    public List<EventDTO> getAllEvents() {
        return eventRepo.findAll().stream().map(this::toDTO).toList();
    }

    public EventDTO updateEvent(Long id, Event updated) {
        Event event = eventRepo.findById(id).orElseThrow(() -> new NotFoundException("Evento non trovato"));
        if (!event.getCreator().getEmail().equals(getCurrentUser().getEmail()))
            throw new IllegalStateException("Non puoi modificare un evento che non hai creato");

        event.setTitle(updated.getTitle());
        event.setDescription(updated.getDescription());
        event.setDate(updated.getDate());
        event.setLocation(updated.getLocation());
        event.setSeatsAvailable(updated.getSeatsAvailable());
        return toDTO(eventRepo.save(event));
    }

    public void deleteEvent(Long id) {
        Event event = eventRepo.findById(id).orElseThrow(() -> new NotFoundException("Evento non trovato"));
        if (!event.getCreator().getEmail().equals(getCurrentUser().getEmail()))
            throw new IllegalStateException("Non puoi eliminare un evento che non hai creato");
        eventRepo.delete(event);
    }

    public EventDTO bookEvent(Long id) {
        Event event = eventRepo.findById(id).orElseThrow(() -> new NotFoundException("Evento non trovato"));
        User currentUser = getCurrentUser();
        if (event.getParticipants().contains(currentUser))
            throw new IllegalStateException("Hai già prenotato questo evento");
        if (event.getSeatsAvailable() <= 0)
            throw new IllegalStateException("Posti esauriti");

        event.getParticipants().add(currentUser);
        currentUser.getBookedEvents().add(event);
        event.setSeatsAvailable(event.getSeatsAvailable() - 1);
        return toDTO(eventRepo.save(event));
    }

    public EventDTO cancelBooking(Long id) {
        Event event = eventRepo.findById(id).orElseThrow(() -> new NotFoundException("Evento non trovato"));
        User currentUser = getCurrentUser();
        if (!event.getParticipants().contains(currentUser))
            throw new IllegalStateException("Non hai prenotato questo evento");

        event.getParticipants().remove(currentUser);
        currentUser.getBookedEvents().remove(event);
        event.setSeatsAvailable(event.getSeatsAvailable() + 1);
        return toDTO(eventRepo.save(event));
    }

    public List<EventDTO> getMyBookings() {
        return getCurrentUser().getBookedEvents().stream().map(this::toDTO).toList();
    }

    public List<EventDTO> getMyCreatedEvents() {
        return getCurrentUser().getCreatedEvents().stream().map(this::toDTO).toList();
    }

    public UserDTO getCurrentUserDTO() {
        User user = getCurrentUser();
        return toUserDTO(user);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente non trovato"));
    }

    private EventDTO toDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .date(event.getDate())
                .location(event.getLocation())
                .seatsAvailable(event.getSeatsAvailable())
                .creatorEmail(event.getCreator().getEmail())
                .participantsEmails(event.getParticipants().stream().map(User::getEmail).toList())
                .build();
    }

    private UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .createdEvents(user.getCreatedEvents().stream().map(this::toDTO).toList())
                .bookedEvents(user.getBookedEvents().stream().map(this::toDTO).toList())
                .build();
    }
}





