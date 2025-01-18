package cat.itacademy.s05.t01.n01.controller;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.GameAction;
import cat.itacademy.s05.t01.n01.service.GameServiceImpl;
import cat.itacademy.s05.t01.n01.service.PlayerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(summary = "Create new game",
            description = "Create and initializes new game by entering player name",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Enter the name of the new player",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @Schema(type = "string", example = "Juan")
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request, error while creating the game"),
    })
    @PostMapping("/new")
    public Mono<ResponseEntity<Game>> createNewGame(
            @Schema(description = "Enter new player name", example = "Juan")
            @RequestBody String playerName) {
        return gameService.createGame(playerName)
                .map(game -> ResponseEntity.status(HttpStatus.CREATED).body(game));
    }

    @Operation(summary = "Make an action in the play",
            description = "Make an action in the play",
            parameters = {@Parameter(
                    name = "id",
                    description = "Unique identifier of the game",
                    required = true,
                    example = "6784e92ceb16d60e2080a472")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Enter the action to be performed (BET, HIT, STAND) and the bet amount (only if action = BET)",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @Schema(implementation = GameAction.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad request in performing the action"),
            @ApiResponse(responseCode = "409", description = "Existing conflict in the state of the game"),

    })
    @PostMapping("/{id}/play")
    Mono<ResponseEntity<Game>> playGame(@PathVariable String id,
                                        @Valid @RequestBody GameAction gameAction){
        return gameService.playGame(id, gameAction.getAction(), gameAction.getBetAmount())
                .map(game -> ResponseEntity.status(HttpStatus.OK).body(game));
    }

    @Operation(summary = "Get game information",
            description = "Get current information of a given game",
            parameters = {@Parameter(
                    name = "id",
                    description = "Unique identifier of the game",
                    required = true,
                    example = "6784e92ceb16d60e2080a472")}
            )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game info successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request, cannot retrieve game info"),
    })
    @GetMapping("/{id}")
    Mono<ResponseEntity<Game>> getGameInfo(@PathVariable String id) {
        return gameService.getGame(id)
                .map(game -> ResponseEntity.status(HttpStatus.OK).body(game));
    }

    @Operation(summary = "Delete a game",
            description = "Delete a given game",
            parameters = {@Parameter(
                    name = "id",
                    description = "Unique identifier of the game",
                    required = true,
                    example = "6784e92ceb16d60e2080a472")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Game successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request in deleting the game"),
    })
    @DeleteMapping("/{id}/delete")
    Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id){
        return gameService.deleteGame(id).thenReturn(ResponseEntity.noContent().build());
    }
}
