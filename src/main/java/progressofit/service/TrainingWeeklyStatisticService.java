package progressofit.service;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progressofit.model.trainingdata.TrainingWeeklyStatistic;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingWeeklyStatisticService extends GenericCrudService<TrainingWeeklyStatistic, Long> {

    /**
     * Busca todas as estatísticas de um usuário
     * @param userId ID do usuário
     * @return Lista de estatísticas do usuário
     */
    @Transactional(readOnly = true)
    public List<TrainingWeeklyStatistic> findByUserId(Long userId) {
        String jpql = "SELECT t FROM TrainingWeeklyStatistic t WHERE t.userId = :userId ORDER BY t.weekStart DESC";
        return executeQuery(jpql, "userId", userId);
    }

    /**
     * Busca estatísticas de um usuário em um período específico
     * @param userId ID do usuário
     * @param startDate Data inicial (inclusive)
     * @param endDate Data final (inclusive)
     * @return Lista de estatísticas no período
     */
    @Transactional(readOnly = true)
    public List<TrainingWeeklyStatistic> findByUserIdAndWeekStartBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT t FROM TrainingWeeklyStatistic t " +
                "WHERE t.userId = :userId " +
                "AND t.weekStart >= :startDate " +
                "AND t.weekStart <= :endDate " +
                "ORDER BY t.weekStart ASC";
        return executeQuery(jpql, "userId", userId, "startDate", startDate, "endDate", endDate);
    }

    /**
     * Busca estatística específica de um usuário em uma semana
     * @param userId ID do usuário
     * @param weekStart Data de início da semana
     * @return Optional contendo a estatística ou vazio se não encontrada
     */
    @Transactional(readOnly = true)
    public Optional<TrainingWeeklyStatistic> findByUserIdAndWeekStart(Long userId, LocalDate weekStart) {
        try {
            String jpql = "SELECT t FROM TrainingWeeklyStatistic t WHERE t.userId = :userId AND t.weekStart = :weekStart";
            TypedQuery<TrainingWeeklyStatistic> query = getEntityManager().createQuery(jpql, TrainingWeeklyStatistic.class);
            query.setParameter("userId", userId);
            query.setParameter("weekStart", weekStart);

            List<TrainingWeeklyStatistic> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar estatística por usuário e semana: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza ou cria uma estatística para um usuário e semana específica (upsert)
     * @param statistic Estatística a ser salva/atualizada
     * @return Estatística processada
     */
    @Transactional
    public TrainingWeeklyStatistic upsertByUserIdAndWeekStart(TrainingWeeklyStatistic statistic) {
        try {
            Optional<TrainingWeeklyStatistic> existing = findByUserIdAndWeekStart(
                    statistic.getUserId(),
                    statistic.getWeekStart()
            );

            if (existing.isPresent()) {
                // Atualiza registro existente
                TrainingWeeklyStatistic existingStatistic = existing.get();
                existingStatistic.setCount(statistic.getCount());
                return update(existingStatistic);
            } else {
                // Cria novo registro
                return save(statistic);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upsert da estatística: " + e.getMessage(), e);
        }
    }

    /**
     * Remove uma estatística específica de um usuário em uma semana
     * @param userId ID do usuário
     * @param weekStart Data de início da semana
     * @return true se removido com sucesso, false se não encontrado
     */
    @Transactional
    public boolean deleteByUserIdAndWeekStart(Long userId, LocalDate weekStart) {
        try {
            Optional<TrainingWeeklyStatistic> statistic = findByUserIdAndWeekStart(userId, weekStart);
            if (statistic.isPresent()) {
                delete(statistic.get());
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar estatística por usuário e semana: " + e.getMessage(), e);
        }
    }

    /**
     * Remove todas as estatísticas de um usuário
     * @param userId ID do usuário
     * @return Número de registros removidos
     */
    @Transactional
    public int deleteByUserId(Long userId) {
        try {
            String jpql = "DELETE FROM TrainingWeeklyStatistic t WHERE t.userId = :userId";
            return getEntityManager().createQuery(jpql)
                    .setParameter("userId", userId)
                    .executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar estatísticas por usuário: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica se existe estatística para um usuário em uma semana específica
     * @param userId ID do usuário
     * @param weekStart Data de início da semana
     * @return true se existe, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndWeekStart(Long userId, LocalDate weekStart) {
        return findByUserIdAndWeekStart(userId, weekStart).isPresent();
    }

    /**
     * Conta o total de estatísticas de um usuário
     * @param userId ID do usuário
     * @return Número total de estatísticas do usuário
     */
    @Transactional(readOnly = true)
    public long countByUserId(Long userId) {
        try {
            String jpql = "SELECT COUNT(t) FROM TrainingWeeklyStatistic t WHERE t.userId = :userId";
            return getEntityManager().createQuery(jpql, Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar estatísticas por usuário: " + e.getMessage(), e);
        }
    }

    /**
     * Busca as últimas N estatísticas de um usuário
     * @param userId ID do usuário
     * @param limit Número de registros a retornar
     * @return Lista das últimas estatísticas do usuário
     */
    @Transactional(readOnly = true)
    public List<TrainingWeeklyStatistic> findLastNByUserId(Long userId, int limit) {
        try {
            String jpql = "SELECT t FROM TrainingWeeklyStatistic t " +
                    "WHERE t.userId = :userId " +
                    "ORDER BY t.weekStart DESC";
            return getEntityManager().createQuery(jpql, TrainingWeeklyStatistic.class)
                    .setParameter("userId", userId)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar últimas estatísticas: " + e.getMessage(), e);
        }
    }
}