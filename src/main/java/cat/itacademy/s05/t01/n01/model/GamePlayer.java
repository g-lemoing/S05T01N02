package cat.itacademy.s05.t01.n01.model;

import java.util.ArrayList;
import java.util.List;

public class GamePlayer {
    private Player player;
    private List<Card> playerHand;
    private int betAmount;

    public GamePlayer() {
    }

    public GamePlayer(Player player) {
        this.player = player;
        this.playerHand = new ArrayList<>();
        this.betAmount = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Card> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(List<Card> playerHand) {
        this.playerHand = playerHand;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public void addCardToPlayerHand(Card card){
        this.playerHand.add(card);
    }

    @Override
    public String toString() {
        return "GamePlayer{" +
                "player=" + player +
                ", playerHand=" + playerHand +
                ", betAmount=" + betAmount +
                "€}";
    }
}
