package it.epicode.appEventi.repositories;

import it.epicode.appEventi.models.Event;
import it.epicode.appEventi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCreator(User creator);
}
