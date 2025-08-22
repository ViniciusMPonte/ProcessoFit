package itinerario.controle_treino.controller;

import itinerario.controle_treino.model.user.User;
import itinerario.controle_treino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> criarUsuario(@RequestBody User user) {
        try {
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest().build();
            }   

            User novoUser = userService.save(user);
            return ResponseEntity.ok(novoUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> listarTodos() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/ordenados")
    public ResponseEntity<List<User>> listarOrdenadosPorNome() {
        List<User> users = userService.findAllOrderByName();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> buscarPorEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar/{nome}")
    public ResponseEntity<List<User>> buscarPorNome(@PathVariable String nome) {
        List<User> users = userService.findByNameContaining(nome);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
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
    public ResponseEntity<User> atualizarNome(@PathVariable Long id, @RequestBody String novoNome) {
        try {
            User userAtualizado = userService.updateUserName(id, novoNome);
            return ResponseEntity.ok(userAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<User> atualizarEmail(@PathVariable Long id, @RequestBody String novoEmail) {
        try {
            User userAtualizado = userService.updateUserEmail(id, novoEmail);
            return ResponseEntity.ok(userAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
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
    public ResponseEntity<List<User>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<User> users = userService.findAll(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> contarUsuarios() {
        long count = userService.count();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> emailExiste(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count/domain/{domain}")
    public ResponseEntity<Long> contarPorDominio(@PathVariable String domain) {
        long count = userService.countByEmailDomain(domain);
        return ResponseEntity.ok(count);
    }
}
