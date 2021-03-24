package nl.hu.cisq1.lingo.trainer.presentation.controller;

import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController {

    /**
     * Handles all other WordLengthNotSupported exception
     */
    @ExceptionHandler(value = WordLengthNotSupportedException.class)
    public ResponseEntity<Map<String, String>> wlnse(Exception e) {
        HashMap<String, String> map = new HashMap<>();
        e.printStackTrace();
        map.put("Error", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other Exceptions to avoid crashes
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, String>> e(Exception e) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Error", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }
}