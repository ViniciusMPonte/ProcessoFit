
package progressofit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progressofit.model.trainingdata.TrainingWeeklyStatistic;
import progressofit.service.TrainingWeeklyStatisticService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/training-weekly-statistics")
@CrossOrigin(origins = "*")
public class TrainingWeeklyStatisticController {

    @Autowired
    private TrainingWeeklyStatisticService service;

    // GET - Buscar todas as estatísticas
    @GetMapping
    public ResponseEntity<List<TrainingWeeklyStatistic>> getAllStatistics() {
        List<TrainingWeeklyStatistic> statistics = service.findAll();
        return ResponseEntity.ok(statistics);
    }

    // GET - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<TrainingWeeklyStatistic> getStatisticById(@PathVariable Long id) {
        Optional<TrainingWeeklyStatistic> statistic = service.findById(id);
        return statistic.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Buscar por usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TrainingWeeklyStatistic>> getStatisticsByUserId(@PathVariable Long userId) {
        List<TrainingWeeklyStatistic> statistics = service.findByUserId(userId);
        return ResponseEntity.ok(statistics);
    }

    // GET - Buscar por usuário e período
    @GetMapping("/user/{userId}/period")
    public ResponseEntity<List<TrainingWeeklyStatistic>> getStatisticsByUserIdAndPeriod(
            @PathVariable Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<TrainingWeeklyStatistic> statistics = service.findByUserIdAndWeekStartBetween(userId, startDate, endDate);
        return ResponseEntity.ok(statistics);
    }

    // GET - Buscar por usuário e semana específica
    @GetMapping("/user/{userId}/week/{weekStart}")
    public ResponseEntity<TrainingWeeklyStatistic> getStatisticByUserIdAndWeek(
            @PathVariable Long userId,
            @PathVariable LocalDate weekStart) {
        Optional<TrainingWeeklyStatistic> statistic = service.findByUserIdAndWeekStart(userId, weekStart);
        return statistic.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Criar nova estatística
    @PostMapping
    public ResponseEntity<TrainingWeeklyStatistic> createStatistic(@RequestBody TrainingWeeklyStatistic statistic) {
        try {
            TrainingWeeklyStatistic savedStatistic = service.save(statistic);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStatistic);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // PUT - Atualizar estatística existente
    @PutMapping("/{id}")
    public ResponseEntity<TrainingWeeklyStatistic> updateStatistic(
            @PathVariable Long id,
            @RequestBody TrainingWeeklyStatistic statistic) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        statistic.setId(id);
        try {
            TrainingWeeklyStatistic updatedStatistic = service.save(statistic);
            return ResponseEntity.ok(updatedStatistic);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // PUT - Atualizar ou criar estatística por usuário e semana
    @PutMapping("/user/{userId}/week/{weekStart}")
    public ResponseEntity<TrainingWeeklyStatistic> upsertStatistic(
            @PathVariable Long userId,
            @PathVariable LocalDate weekStart,
            @RequestBody TrainingWeeklyStatistic statistic) {

        statistic.setUserId(userId);
        statistic.setWeekStart(weekStart);

        TrainingWeeklyStatistic savedStatistic = service.upsertByUserIdAndWeekStart(statistic);
        return ResponseEntity.ok(savedStatistic);
    }

    // DELETE - Deletar por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatistic(@PathVariable Long id) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE - Deletar por usuário e semana
    @DeleteMapping("/user/{userId}/week/{weekStart}")
    public ResponseEntity<Void> deleteStatisticByUserAndWeek(
            @PathVariable Long userId,
            @PathVariable LocalDate weekStart) {

        boolean deleted = service.deleteByUserIdAndWeekStart(userId, weekStart);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // DELETE - Deletar todas as estatísticas de um usuário
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteAllStatisticsByUser(@PathVariable Long userId) {
        service.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}