package processofit.controller;

import processofit.model.user.User;
import processofit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> listarTodos() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.findByEmail(authentication.name).id == #id")
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.findByEmail(authentication.name).id == #id")
    public ResponseEntity<User> atualizarUsuario(@PathVariable Long id, @RequestBody User user) {
        try {
            if (!userService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            User existingUser = userService.findByEmail(user.getEmail());
            if (existingUser != null && !existingUser.getId().equals(id)) {
                return ResponseEntity.badRequest().build();
            }

            user.setId(id);
            User userAtualizado = userService.update(user);
            return ResponseEntity.ok(userAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/nome")
    @PreAuthorize("hasRole('ADMIN') or @userService.findByEmail(authentication.name).id == #id")
    public ResponseEntity<User> atualizarNome(@PathVariable Long id, @RequestBody String novoNome) {
        try {
            User userAtualizado = userService.updateUserName(id, novoNome);
            return ResponseEntity.ok(userAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/email")
    @PreAuthorize("hasRole('ADMIN') or @userService.findByEmail(authentication.name).id == #id")
    public ResponseEntity<User> atualizarEmail(@PathVariable Long id, @RequestBody String novoEmail) {
        try {
            User userAtualizado = userService.updateUserEmail(id, novoEmail);
            return ResponseEntity.ok(userAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.findByEmail(authentication.name).id == #id")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        try {
            if (userService.deleteById(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<User> users = userService.findAll(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> contarUsuarios() {
        long count = userService.count();
        return ResponseEntity.ok(count);
    }
}