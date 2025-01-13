package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GamePlayer;
import reactor.core.publisher.Mono;

public interface GameService {
    Mono<Game> createGame(String name);
    Mono<Game> getGame(String id);
    Mono<Void> deleteGame(String gameId);
    Mono<GamePlayer> updateName(String newName, GamePlayer gamePlayer);
}
