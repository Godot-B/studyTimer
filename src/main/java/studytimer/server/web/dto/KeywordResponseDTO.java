package studytimer.server.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class KeywordResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetResultDTO {

        @Builder.Default
        String message = "타이머 시작 성공";

        Long keywordId;
        String keywordName;
        LocalDateTime createdAt;
    }
}
