package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
public class PlayerServiceImpl implements PlayerService{
    private static final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Mono<Player> save(Player player) {
        return this.playerRepository.save(player);
    }

    @Override
    public Flux<Player> getPlayersSorted() {
        return this.playerRepository.findAll()
                .sort(Comparator.comparingDouble(Player::getScore).reversed())
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<Player> updatePlayerName(int id, String newName) {
        return findPlayerById(id).map(p -> {
            p.setName(newName);
            return p;
        }).flatMap(p ->
            this.playerRepository.save(p)).doOnSuccess(p ->
                log.info("Player name successfully updated to {} with playerId {}", p.getName(), p.getId()));
    }

    @Override
    public Mono<Player> updatePlayerScore(Player player, double prizeAmount) {
        player.setScore(player.getScore() + prizeAmount);
        return this.playerRepository.save(player).doOnSuccess(p ->
            log.info("Player score successfully updated to {} with playerId {}", p.getScore(), p.getId()));
    }

    @Override
    public Mono<Player> createNewPlayer(String name){
        return this.playerRepository.findPlayerByName(name)
                .doOnSuccess(player -> log.info("Found player: {}", player))
                .switchIfEmpty(this.playerRepository.save(new Player(name)))
                .doOnSuccess(p ->
                        log.info("Player successfully created with name {} and id {}", p.getName(), p.getId()));
    }

    @Override
    public Mono<Player> findPlayerById(int id){
        return this.playerRepository.findById(id)
                .doOnNext(player -> log.info("Found player by id: {}", player))
                .switchIfEmpty(Mono.error(new PlayerNotFoundException(id)));
    }
}
