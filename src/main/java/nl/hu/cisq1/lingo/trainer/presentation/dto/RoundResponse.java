package nl.hu.cisq1.lingo.trainer.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoundResponse {
    private final List<Character> hint;
    private final String guess;

    public RoundResponse(List<Character> hint, String guess) {
        this.hint = hint;
        this.guess = guess;
    }

    public List<Character> getHint() {
        return hint;
    }

    public String getGuess() {
        return guess;
    }
}
