package nl.hu.cisq1.lingo.words.application;

import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * This is a unit test.
 *
 * It tests the behaviors of our system under test,
 * WordService, in complete isolation:
 * - its methods are called by the test framework instead of a controller
 * - the WordService calls a test double instead of an actual repository
 */
class WordServiceTest {
    private static SpringWordRepository mockRepository;
    private static WordService service;

    @BeforeAll
    static void init() {
        mockRepository = mock(SpringWordRepository.class);
        service = new WordService(mockRepository);
    }

    @ParameterizedTest
    @DisplayName("requests a random word of a specified length from the repository")
    @MethodSource("randomWordExamples")
    void providesRandomWord(int wordLength, String word) {
        when(mockRepository.findRandomWordByLength(wordLength))
                .thenReturn(Optional.of(new Word(word)));

        String result = service.provideRandomWord(wordLength);

        assertEquals(word, result);
    }

    @Test
    @DisplayName("throws exception if length not supported")
    void unsupportedLength() {
        when(mockRepository.findRandomWordByLength(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(
                WordLengthNotSupportedException.class,
                () -> service.provideRandomWord(5)
        );
    }

    static Stream<Arguments> randomWordExamples() {
        return Stream.of(
                Arguments.of(5, "tower"),
                Arguments.of(6, "castle"),
                Arguments.of(7, "knights")
        );
    }

    @ParameterizedTest
    @MethodSource("provideWords")
    @DisplayName("Word exists returns expected boolean")
    void wordExists_ReturnsExpectedBoolean(String word, boolean expectedBoolean ) {
        when(mockRepository.findWordByValue("woord"))
                .thenReturn(Optional.of(new Word("woord")));

        boolean exists = service.wordExists(word);

        assertEquals(expectedBoolean, exists);
    }
    static Stream<Arguments> provideWords() {
        return Stream.of(
                Arguments.of("woord", true),
                Arguments.of("bbbbb", false)
        );
    }
}