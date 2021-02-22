package nl.hu.cisq1.lingo.trainer.domain.exception;

public class InvalidHintException extends RuntimeException{

    public InvalidHintException() { }

    public InvalidHintException(String message) {
        super(message);
    }
}
