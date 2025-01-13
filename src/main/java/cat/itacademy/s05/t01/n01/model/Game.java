package cat.itacademy.s05.t01.n01.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "game")
public class Game {
    @Id
    private String _id;
    private GamePlayer gamePlayer;
    private GameStatus gameStatus;
    private List<Card> bankHand;
    private CardDeck cardDeck;

    public Game() {
    }

    public Game(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        this.gameStatus = GameStatus.INIT;
        this.bankHand = new ArrayList<>();
        this.cardDeck = new CardDeck();
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<Card> getBankHand() {
        return bankHand;
    }

    public void setBankHand(List<Card> bankHand) {
        this.bankHand = bankHand;
    }

    public CardDeck getCardDeck() {
        return cardDeck;
    }

    public void setCardDeck(CardDeck cardDeck) {
        this.cardDeck = cardDeck;
    }
}
