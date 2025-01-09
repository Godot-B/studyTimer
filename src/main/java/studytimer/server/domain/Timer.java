package studytimer.server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import studytimer.server.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Timer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dateplan_id", nullable = false)
    private DatePlan datePlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    private String timerName;

    private Integer timerGoalTime; // 목표 공부 시간

    private Float timerStudyTime = 0.0f; // 실제 공부 시간

    private Integer breakTime = 10; // 휴식 시간 (현재 10분 고정)

    private Boolean started = false;

    private Boolean completed = false;

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public void setDatePlan(DatePlan datePlan) {
        this.datePlan = datePlan;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public void updateTimerStudyTime(Float timerStudyTime) {
        this.timerStudyTime += timerStudyTime;
        if (this.timerStudyTime >= timerGoalTime)
            completed = true;
        keyword.updateSubjectStudyTime(timerStudyTime);
        datePlan.updateTotalStudyTime(timerStudyTime);
    }

    // 자정 이벤트 위한 깊은 복사
    public Timer copy() {
        return Timer.builder()
                .keyword(keyword)
                .timerName(timerName)
                .timerGoalTime(timerGoalTime)
                .timerStudyTime(timerStudyTime)
                .started(started)
                .completed(completed)
                .build();
    }
}