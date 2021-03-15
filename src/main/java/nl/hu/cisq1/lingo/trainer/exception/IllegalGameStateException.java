package nl.hu.cisq1.lingo.trainer.exception;

public class IllegalGameStateException extends IllegalStateException{
    public IllegalGameStateException() { }

    public IllegalGameStateException(String s) {
        super(s);
    }
}
