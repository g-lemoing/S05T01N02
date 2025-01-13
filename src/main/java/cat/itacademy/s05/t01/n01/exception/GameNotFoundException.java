package cat.itacademy.s05.t01.n01.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String idGame) {
        super("Game not found with id " + idGame);
    }
}
