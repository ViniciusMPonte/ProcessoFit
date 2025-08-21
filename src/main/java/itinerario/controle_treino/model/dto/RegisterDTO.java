package itinerario.controle_treino.model.dto;

import itinerario.controle_treino.model.role.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {
}
