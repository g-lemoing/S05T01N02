package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.exception.GameNotFoundException;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GamePlayer;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameServiceImpl implements GameService{

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired PlayerServiceImpl playerService;

    private static Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Override
    public Mono<Game> createGame(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Player name cannot be null or empty");

        return this.playerService.createNewPlayer(name).flatMap(
                player -> {
                    GamePlayer gamePlayer = new GamePlayer(player);
                    log.info(String.valueOf(player.getName()));
                    Game game = new Game(gamePlayer);
                    return gameRepository.save(game).doOnNext(
                            createdGame -> log.info("Game successfully initialized with id: "
                                    + createdGame.getId() + " and player " + player.getName())
                    );
                }
        );
    }

    @Override
    public Mono<Game> getGame(String id) {
        if(id == null || id.isEmpty()) throw new IllegalArgumentException("Id game is mandatory.");
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)));
    }

    @Override
    public Mono<Void> deleteGame(String gameId) {
        if(gameId == null || gameId.isEmpty()) throw new IllegalArgumentException("Id game is mandatory.");
        return getGame(gameId).flatMap(game -> gameRepository.delete(game));
    }

    @Override
    public Mono<GamePlayer> updateName(String newName, GamePlayer gamePlayer) {
        return null;
    }
}
