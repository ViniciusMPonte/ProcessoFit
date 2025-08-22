package itinerario.controle_treino.model.user.dto;

import itinerario.controle_treino.model.user.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {
}
