package cat.itacademy.s05.t01.n01.exception;

public class EmptyCardDeckException extends RuntimeException {
    public EmptyCardDeckException(String message) {
        super("No cards left in the deck.");
    }
}
