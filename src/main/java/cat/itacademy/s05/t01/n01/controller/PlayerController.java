package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.service.PlayerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class PlayerController {

    @Autowired
    private PlayerServiceImpl playerService;

    @Operation(summary = "Modify player name",
            description = "Modify player name of given player id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Enter the name of the new player",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @Schema(type = "string", example = "Juan")
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player name successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad request in updating player name"),
    })
    @PutMapping(value = {"/player/{id}"})
    public Mono<ResponseEntity<Player>> updatePlayerName(@Parameter(description = "Enter player id") @PathVariable Integer id,
                                                         @RequestBody @Schema(description = "Enter new player name", example = "Juan") String newName){
        return playerService.updatePlayerName(id, newName).map(player ->
                ResponseEntity.status(HttpStatus.OK).body(player));
    }

    @GetMapping(value = {"/ranking"})
    @Operation(summary = "Get players sorted by descending score")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking of players successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request in retrieving player list"),
    })
    public Mono<ResponseEntity<List<Player>>> getPlayersRanking(){
        return playerService.getPlayersSorted().collectList().map(players ->
                ResponseEntity.status(HttpStatus.OK).body(players));
    }
}
