package studytimer.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import studytimer.server.domain.Timer;

public interface TimerRepository extends JpaRepository<Timer, Long> {
}
