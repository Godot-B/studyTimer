package studytimer.server.repository;


import studytimer.server.domain.DatePlan;

import java.time.LocalDate;

public interface DatePlanRepositoryCustom {
    DatePlan findByDateAndThrow(LocalDate date);
}
