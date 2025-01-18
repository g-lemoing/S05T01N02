package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.exception.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.service.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerControllerTest {
    @Mock
    private PlayerServiceImpl playerService;

    @InjectMocks
    private PlayerController playerController;

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
    void updatePlayerNameWhenPlayerExistsTest() {
        String newName = "Gwen";
        Player expectedPlayer = new Player(player1.getId(), newName, player1.getScore());
        when(playerService.updatePlayerName(player1.getId(), newName))
                .thenReturn(Mono.just(expectedPlayer));
        StepVerifier.create(playerController.updatePlayerName(player1.getId(), newName))
                .expectNextMatches(playerResponseEntity ->
                        playerResponseEntity.getStatusCode().equals(HttpStatus.OK) &&
                                Objects.equals(playerResponseEntity.getBody(), expectedPlayer))
                .verifyComplete();
    }

    @Test
    void updatePlayerNameWhenPlayerNotExistsTest() {
        String newName = "Gwen";
        int playerId = 4444;
        when(playerService.updatePlayerName(playerId, newName))
                .thenReturn(Mono.error(new PlayerNotFoundException(playerId)));
        StepVerifier.create(playerController.updatePlayerName(playerId, newName))
                .expectError(PlayerNotFoundException.class).verify();
    }

    @Test
    void getPlayersRankingWhenPlayersListIsNotEmptyTest() {
        when(playerService.getPlayersSorted()).thenReturn(Flux.fromIterable(players));
        StepVerifier.create(playerController.getPlayersRanking())
                .expectNextMatches(playerResponseEntity -> playerResponseEntity.getStatusCode().equals(HttpStatus.OK)
                    && Objects.equals(playerResponseEntity.getBody(), players))
                .verifyComplete();
    }

    @Test
    void getPlayersRankingWhenPlayersListIsEmptyTest() {
        when(playerService.getPlayersSorted()).thenReturn(Flux.empty());
        StepVerifier.create(playerController.getPlayersRanking()).expectNextMatches(
                playerResponseEntity -> playerResponseEntity.getStatusCode().equals(HttpStatus.OK)
                        && Objects.requireNonNull(playerResponseEntity.getBody()).isEmpty())
                .verifyComplete();
    }
}