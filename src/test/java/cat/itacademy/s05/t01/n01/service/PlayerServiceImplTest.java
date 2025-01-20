package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    List<Player> players;
    Player player1;
    Player player2;
    Player player3;

    @BeforeEach
    void initTest(){
        player1 = new Player(1, "Juan", 10.00);
        player2 = new Player(2, "Ana", 50.00);
        player3 = new Player(3, "Pere", 0.00);
        players = List.of(player1, player2, player3);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPlayersSortedTest() {
        when(playerRepository.findAll()).thenReturn(Flux.fromIterable(players));

        StepVerifier.create(playerService.getPlayersSorted())
                .expectNextMatches(player -> player.getName().equals(player2.getName()) && player.getScore() == player2.getScore())
                .expectNextMatches(player -> player.getName().equals(player1.getName()) && player.getScore() == player1.getScore())
                .expectNextMatches(player -> player.getName().equals(player3.getName()) && player.getScore() == player3.getScore())
                .verifyComplete();
    }

    @Test
    void getPlayersSortedWhenPlayerListEmptyTest() {
        when(playerRepository.findAll()).thenReturn(Flux.empty());
        StepVerifier.create(playerService.getPlayersSorted())
                .verifyComplete();
    }

    @Test
    void findPlayerByIdTestOk() {
        when(playerRepository.findById(player1.getId())).thenReturn(Mono.just(player1));
        StepVerifier.create(playerService.findPlayerById(player1.getId()))
                .expectNextMatches(player -> player.getId() == player1.getId())
                .verifyComplete();
    }

    @Test
    void findPlayerByIdTestKo() {
        int idPlayer = 4444;
        when(playerRepository.findById(idPlayer)).thenReturn(Mono.empty());
        StepVerifier.create(playerService.findPlayerById(idPlayer))
                .expectError(PlayerNotFoundException.class).verify();
    }

    @Test
    void updatePlayerNameWhenPlayerExistsTest() {
        String newName = "Gwen";
        Player updatedPlayer = new Player(player1.getId(), newName, player1.getScore());
        when(playerRepository.findById(player1.getId())).thenReturn(Mono.just(player1));
        when(playerRepository.save(ArgumentMatchers.any(Player.class))).thenReturn(Mono.just(updatedPlayer));

        StepVerifier.create(playerService.updatePlayerName(player1.getId(), newName))
                .expectNextMatches(player -> player.getId() == updatedPlayer.getId() &&
                        player.getName().equals(updatedPlayer.getName()) &&
                        player.getScore() == updatedPlayer.getScore())
                .verifyComplete();
    }

    @Test
    void updatePlayerNameWhenPlayerNotExistsTest() {
        int idPlayer = 4444;
        String newName = "Gwen";
        when(playerRepository.findById(idPlayer)).thenReturn(Mono.empty());
        StepVerifier.create(playerService.updatePlayerName(idPlayer, newName))
                .expectError(PlayerNotFoundException.class).verify();
    }

    @Test
    void updatePlayerScoreTest() {
        double prizeAmount  = 10.00;
        Player updatedPlayer = new Player(player1.getId(), player1.getName(), player1.getScore() + prizeAmount);
        when(playerRepository.save(ArgumentMatchers.any(Player.class))).thenReturn(Mono.just(updatedPlayer));

        StepVerifier.create(playerService.updatePlayerScore(player1, prizeAmount))
                .expectNextMatches(player -> player.getId() == updatedPlayer.getId() &&
                        player.getName().equals(updatedPlayer.getName()) &&
                        player.getScore() == updatedPlayer.getScore())
                .verifyComplete();
    }

//    @Test
//    void createNewPlayerWhenPlayerExistsTest() {
//        String name = "Juan";
//        Player checkedPlayer = new Player(1, name, 10);
//
//        when(playerRepository.findPlayerByName(ArgumentMatchers.anyString()))
//                .thenReturn(Mono.just(checkedPlayer));
//        Mono<Player> playerMono = playerService.createNewPlayer(name);
//        StepVerifier.create(playerService.createNewPlayer("Juan"))
//            .expectNextMatches(player -> player.getId() == player1.getId() &&
//                    player.getName().equals(player1.getName()) &&
//                    player.getScore() == player1.getScore())
//            .verifyComplete();
//    }

    @Test
    void createNewPlayerWhenPlayerNotExistsTest() {
        String playerName = "Artur";
        Player player = new Player(playerName);
        when(playerRepository.findPlayerByName(playerName)).thenReturn(Mono.empty());
        when(playerRepository.save(ArgumentMatchers.any(Player.class))).thenReturn(Mono.just(player));

        StepVerifier.create(playerService.createNewPlayer(playerName))
                .expectNextMatches(p -> p.getName().equals(player.getName()) &&
                        p.getScore() == 0)
                .verifyComplete();
    }
}