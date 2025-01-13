package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.service.GameServiceImpl;
import cat.itacademy.s05.t01.n01.service.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameServiceImpl gameService;
    @Autowired
    private PlayerServiceImpl playerService;

    @PostMapping("/new")
    public Mono<ResponseEntity<Game>> createNewGame(@RequestBody String playerName) {
        return gameService.createGame(playerName)
                .map(game -> ResponseEntity.status(HttpStatus.CREATED).body(game));
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<Game>> getGameInfo(@PathVariable String id) {
        return gameService.getGame(id)
                .map(game -> ResponseEntity.status(HttpStatus.OK).body(game));
    }

    @DeleteMapping("/{id}/delete")
    Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id){
        return gameService.deleteGame(id).thenReturn(ResponseEntity.noContent().build());
    }
}
