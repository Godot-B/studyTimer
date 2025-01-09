package studytimer.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import studytimer.server.domain.DatePlan;

import java.time.LocalDate;
import java.util.Optional;

public interface DatePlanRepository extends JpaRepository<DatePlan, Long>, DatePlanRepositoryCustom {
    DatePlan findByDate(LocalDate date);
}
