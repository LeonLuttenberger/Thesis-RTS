package hr.fer.zemris.zavrsni.rts.common;

public class NoSuchResourceException extends RuntimeException {

    public NoSuchResourceException() {
    }

    public NoSuchResourceException(String s) {
        super(s);
    }

    public NoSuchResourceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoSuchResourceException(Throwable throwable) {
        super(throwable);
    }
}
