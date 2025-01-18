package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.exception.GameNotFoundException;
import cat.itacademy.s05.t01.n01.model.*;
import cat.itacademy.s05.t01.n01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameServiceImpl implements GameService{

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    PlayerServiceImpl playerService;

    private static Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Override
    public Mono<Game> createGame(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Player name cannot be null or empty");

        return this.playerService.createNewPlayer(name).flatMap(
                player -> {
                    GamePlayer gamePlayer = new GamePlayer(player);
                    Game game = new Game(gamePlayer);
                    return gameRepository.save(game).doOnNext(
                            createdGame -> log.info("Game successfully initialized with id: "
                                    + createdGame.getId() + " and player " + player.getName())
                    );
                }
        );
    }

    @Override
    public Mono<Game> getGame(String id) {
        if(id == null || id.isEmpty()) throw new IllegalArgumentException("Id game is mandatory.");
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)));
    }

    @Override
    public Mono<Void> deleteGame(String gameId) {
        if(gameId == null || gameId.isEmpty()) throw new IllegalArgumentException("Id game is mandatory.");
        return getGame(gameId).flatMap(game -> gameRepository.delete(game));
    }

    @Override
    public Mono<Game> playGame(String gameId, Action action, int betAmount) {
        return getGame(gameId).flatMap(game -> {
            if(game.isGameOver()){
                return Mono.error(new IllegalStateException("This game is over."));
            }
            return playAction(game, action, betAmount);
        });
    }

    @Override
    public Mono<Game> updatePlayerName(String gameId, Player player) {
        return null;
    }

    public Mono<Game> playAction (Game game, Action action, int betAmount){
        switch (action){
            case BET -> {
                return playBet(game, betAmount);
            }
            case HIT -> {
                return playHit(game);
            }
//            case DOUBLE -> {
//
//            }
//            case SECURE -> {
//
//            }
            case STAND -> {
                return playStand(game);
            }
            case null, default -> {
                return Mono.error(new IllegalArgumentException("Action does not exist"));
            }
        }
    }

    Mono<Game> playBet(Game game, int betAmount){
        if(game.getGameStatus() != GameStatus.INIT) throw new IllegalArgumentException("You cannot bet at this moment.");
        if(betAmount == 0) throw new IllegalArgumentException("Bet amount must be greater than 0.");

        game.setBetAmount(betAmount);
        game.initialCardDeal();
        if(Game.getHandValue(game.getGamePlayer().getPlayerHand()) > 21){
            game.setGameStatus(GameStatus.BANK_WINS);
        } else {
            game.setGameStatus(GameStatus.SECOND_ROUND);
        }
        return gameRepository.save(game);
    }

    Mono<Game> playHit(Game game){
        if(game.getGameStatus() != GameStatus.SECOND_ROUND) throw new IllegalArgumentException("You cannot hit for another card at this moment.");

        game.givePlayerHand(1);
        if(Game.getHandValue(game.getGamePlayer().getPlayerHand()) > 21){
            game.setGameStatus(GameStatus.BANK_WINS);
        }
        return gameRepository.save(game);
    }

    Mono<Game> playStand(Game game) {
        if (game.getGameStatus() != GameStatus.SECOND_ROUND)
            throw new IllegalArgumentException("You cannot stand at this moment.");

        while (Game.getHandValue(game.getBankHand()) < 17) {
            game.giveBankHand(1);
        }
        GameStatus gameStatus = game.ckeckWinner();
        game.setGameStatus(gameStatus);
        if (gameStatus == GameStatus.PLAYER_WINS || gameStatus == GameStatus.DRAW) {
            double prizeAmount = game.getplayerPrize();
            return this.playerService.updatePlayerScore(game.getGamePlayer().getPlayer(), prizeAmount)
                    .flatMap(player -> {
                        log.info("Player " + player.getName() + " wins the game! Prize amount: "
                                + prizeAmount + ", new score: " + player.getScore());
                        return gameRepository.save(game);
                    });
        }
        return gameRepository.save(game);
    }
}
