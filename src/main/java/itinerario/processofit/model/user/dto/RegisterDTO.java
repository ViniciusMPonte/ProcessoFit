package itinerario.processofit.model.user.dto;

import itinerario.processofit.model.user.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {
}
