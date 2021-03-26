package nl.hu.cisq1.lingo.trainer.domain.game.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultWordLengthStrategy implements WordLengthStrategy {
    private static final int DEFAULT_INITIAL_WORD_LENGTH = 5;
    private final HashMap<Integer, Integer> wordLengths = new HashMap<>(
            Map.of(
                    5, 6,
                    6, 7,
                    7, DEFAULT_INITIAL_WORD_LENGTH
            )
    );

    @Override
    public Integer next(Integer currentWordLength) {
        return Optional.ofNullable(wordLengths.get(currentWordLength))
                .orElse(DEFAULT_INITIAL_WORD_LENGTH);
    }
}
