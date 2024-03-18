package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.card.Deck;
import it.polimi.ingsw.server.model.objectives.Objective;

import java.util.List;

public class Table {
    private ScoreTrack scoreTrack;
    private List<Deck> decks;
    private List<Objective> objectives;
    private List<Card> cardsOnGround;

    public ScoreTrack getScoreTrack() {
        return scoreTrack;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public List<Objective> getObjectives() {
        return objectives;
    }

    public List<Card> getCardsOnGround() {
        return cardsOnGround;
    }
}