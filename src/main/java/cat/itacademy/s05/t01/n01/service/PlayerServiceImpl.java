package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameServiceImpl gameService;

    @Override
    public Mono<Player> save(Player player) {
        return this.playerRepository.save(player);
    }

    @Override
    public Flux<Player> getPlayersSorted() {
        return this.playerRepository.findAll()
                .sort(Comparator.comparingDouble(Player::getScore).reversed())
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<Player> update(int id, String newName) {
        return findPlayerById(id).map(p -> {
            p.setName(newName);
            return p;
        }).flatMap(p ->
            this.playerRepository.save(p));
    }

    @Override
    public Mono<Player> updatePlayerScore(Player player, double prizeAmount) {
        player.setScore(player.getScore() + prizeAmount);
        return this.playerRepository.save(player);
    }

    public Mono<Player> createNewPlayer(String name){
        return playerRepository.findPlayerByName(name)
                .switchIfEmpty(this.playerRepository.save(new Player(name)));
    }

    public Mono<Player> findPlayerById(int id){
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException(id)));
    }
}
