package studytimer.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import studytimer.server.domain.Keyword;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByKeywordNameContainingIgnoreCase(String userInput);

    Keyword findByKeywordName(String keywordName);
}
