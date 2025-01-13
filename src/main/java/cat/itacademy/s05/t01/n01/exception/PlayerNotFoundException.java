package cat.itacademy.s05.t01.n01.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(int id) {
        super("Player not found with id:" + id);
    }
}
