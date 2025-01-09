package studytimer.server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import studytimer.server.domain.common.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DatePlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "datePlan", cascade = CascadeType.ALL)
    private List<Timer> timerList = new ArrayList<>();

    private LocalDate date;

    private Integer goalTime;

    private Float totalStudyTime = 0.0f;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "study_time_by_hour")
    private Map<Integer, Integer> hourlyStudyTimes = initializeHourlyMap();

    private static Map<Integer, Integer> initializeHourlyMap() {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            map.put(i, 0);  // 각 시간대 초기값 0으로 설정
        }
        return map;
    }

    public void updateGoalTime(Integer goalTime) {
        this.goalTime = goalTime;
    }

    public void updateTotalStudyTime(Float totalStudyTime) {
        this.totalStudyTime += totalStudyTime;
    }
}
