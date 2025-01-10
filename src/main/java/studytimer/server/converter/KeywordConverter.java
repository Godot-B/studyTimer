package studytimer.server.converter;

import studytimer.server.domain.Keyword;
import studytimer.server.web.dto.KeywordResponseDTO;

public class KeywordConverter {

    public static KeywordResponseDTO.SetResultDTO toSetResultDTO(Keyword keyword) {
        return KeywordResponseDTO.SetResultDTO.builder()
                .keywordId(keyword.getId())
                .keywordName(keyword.getKeywordName())
                .createdAt(keyword.getCreatedAt())
                .build();
    }
}
