package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PlayerService {
    Mono<Player> save(Player player);
    Flux<Player> getPlayersSorted();
    Mono<Player> updatePlayerName(int id, String newName);
    Mono<Player> updatePlayerScore(Player player, double prizeAmount);
    Mono<Player> createNewPlayer(String name);
    Mono<Player> findPlayerById(int id);
}
