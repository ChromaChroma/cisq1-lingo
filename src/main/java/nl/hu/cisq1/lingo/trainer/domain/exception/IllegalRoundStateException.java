package nl.hu.cisq1.lingo.trainer.domain.exception;

public class IllegalRoundStateException extends RuntimeException{
    public IllegalRoundStateException() {
    }

    public IllegalRoundStateException(String message) {
        super(message);
    }
}
