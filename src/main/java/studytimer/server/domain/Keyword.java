package studytimer.server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import studytimer.server.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Keyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "keyword")
    private List<Timer> timers = new ArrayList<>();

    private String keywordName;

    @Builder.Default
    private Float keywordStudyTime = 0.0f; // 실제 공부 시간

    public void updateSubjectStudyTime(Float studyTime) {
        this.keywordStudyTime += studyTime;
    }
}
