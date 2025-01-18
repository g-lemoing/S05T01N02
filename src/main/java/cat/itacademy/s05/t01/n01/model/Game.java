package cat.itacademy.s05.t01.n01.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @JsonIgnore
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

    public void givePlayerHand (int cards){
        for (int i = 0; i < cards; i++){
            this.gamePlayer.addCardToPlayerHand(cardDeck.getCardFromDeck());
        }
    }

    public void giveBankHand (int cards) {
        for (int i = 0; i < cards; i++) {
            this.bankHand.add(cardDeck.getCardFromDeck());
        }
    }

    public void setBetAmount(int betAmount){
        this.gamePlayer.setBetAmount(betAmount);
    }

    public void initialCardDeal(){
        givePlayerHand(2);
        giveBankHand(2);
    }

    public static int getHandValue(List<Card> cardList){
        int score = 0;
        int aces = 0;

        for(Card card: cardList){
            score += card.getCardValue();
            if(card.getRank() == Rank._1) aces++;
        }

        while (score > 21 && aces > 0){
            score -= 10;
            aces--;
        }
        return score;
    }

    public GameStatus ckeckWinner(){
        GameStatus status;
        int playerHandValue = getHandValue(this.getGamePlayer().getPlayerHand());
        int bankHandValue = getHandValue(this.getBankHand());

        if (playerHandValue > 21 || playerHandValue < bankHandValue && bankHandValue < 22){
            status = GameStatus.BANK_WINS;
        } else if (playerHandValue > bankHandValue || bankHandValue > 21) {
            status =  GameStatus.PLAYER_WINS;
        } else status = GameStatus.DRAW;

        return status;
    }

    public double getplayerPrize(){
        int playerHandValue = getHandValue(this.getGamePlayer().getPlayerHand());
        int bankHandValue = getHandValue(this.getBankHand());
        if (playerHandValue == 21){
            return gamePlayer.getBetAmount() * 3 / 2;
        }
        else if (playerHandValue > bankHandValue){
            return gamePlayer.getBetAmount() * 2;
        }else {
            return gamePlayer.getBetAmount();
        }

    }

    public boolean isGameOver(){
        return gameStatus == GameStatus.DRAW || gameStatus == GameStatus.PLAYER_WINS
                || gameStatus == GameStatus.BANK_WINS;
    }

}
