package processofit.model.user.dto;

import processofit.model.user.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {
}
