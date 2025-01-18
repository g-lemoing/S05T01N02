package cat.itacademy.s05.t01.n01.model;

import cat.itacademy.s05.t01.n01.exception.EmptyCardDeckException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {

    private List<Card> cardList;

    public CardDeck() {
        this.cardList = new ArrayList<>();
        initDeck();
    }

    public void initDeck(){
        for (Suit suit: Suit.values()){
            for (Rank rank: Rank.values()){
                this.cardList.add(new Card(suit, rank));
            }
        }
        shuffleDeck();
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public void shuffleDeck(){
        Collections.shuffle(this.cardList);
    }

    @JsonIgnore
    public Card getCardFromDeck(){
        if(this.cardList.isEmpty()) throw new EmptyCardDeckException("No cards left in the deck!");
        return this.cardList.removeFirst();
    }
}
