package mtt.exmaplebackend.exceptioHandler;

public class BadRequestexception extends RuntimeException {
    public BadRequestexception(String message) {
        super(message);
    }
}
