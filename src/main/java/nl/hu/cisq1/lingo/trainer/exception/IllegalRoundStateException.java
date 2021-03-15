package nl.hu.cisq1.lingo.trainer.exception;

public class IllegalRoundStateException extends IllegalStateException{
    public IllegalRoundStateException() { }

    public IllegalRoundStateException(String message) {
        super(message);
    }
}
