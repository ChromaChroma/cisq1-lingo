package nl.hu.cisq1.lingo.trainer.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoundResponse {
    public List<Character> hint;
    public String guess;
}
