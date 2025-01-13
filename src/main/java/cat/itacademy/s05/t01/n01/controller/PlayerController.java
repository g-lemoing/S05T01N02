package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.service.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PlayerController {

    @Autowired
    private PlayerServiceImpl playerService;

    @PutMapping(value = {"/player/{id}", "/player/"})
    public Mono<ResponseEntity<Player>> updatePlayerName(@PathVariable Integer id, @RequestBody String newName){
        return playerService.update(id, newName).map(player ->
                ResponseEntity.status(HttpStatus.OK).body(player));
    }

    @GetMapping(value = {"/ranking"})
    public Flux<Player> getPlayersRanking(){
        return playerService.getPlayersSorted();
    }
}
