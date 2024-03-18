package it.polimi.ingsw.server.model.card;

import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public Deck(int numCards) {
        for (int i = 0; i < numCards; i++) {
            GoldCard card = new GoldCard();
            this.cards.add(card);
        }
        this.shuffleDeck();
    }


    public Card drawCard() {
        return cards.removeFirst();
    }

    public List<Card> getCards() {
        return cards;
    }

}