package it.epicode.appEventi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private List<EventDTO> createdEvents;
    private List<EventDTO> bookedEvents;
}


