package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PlayerService {
    Mono<Player> save(Player player);
    Flux<Player> getPlayersSorted();
    Mono<Player> update(int id, String newName);
}
