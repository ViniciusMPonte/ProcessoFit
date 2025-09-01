package progressofit.model.trainingdata;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "training_weekly_statistics", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "week_start"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingWeeklyStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable =  false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @Column(name = "count", nullable = false)
    private Integer count;
}