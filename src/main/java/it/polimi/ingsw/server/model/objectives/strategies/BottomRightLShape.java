package it.polimi.ingsw.server.model.objectives.strategies;

import it.polimi.ingsw.server.model.card.CardColorEnum;
import it.polimi.ingsw.server.model.card.ResourceCard;
import it.polimi.ingsw.server.model.card.StarterCard;
import it.polimi.ingsw.server.model.objectives.ObjectiveStrategy;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;

/**
 * This class implements the ObjectiveStrategy and represents the strategy for the objective "BOTTOM_RIGHT_L_SHAPE".
 * It calculates the points of the player based on the number of bottom right L shapes
 *      of the specified color in their card matrix
 */
public class BottomRightLShape implements ObjectiveStrategy {

    /**
     * The color of the 2 cards that form the column of the L shape
     */
    private final CardColorEnum columnCardsColor;

    /**
     * The color of the card that's at the bottom right of the L shape
     */
    private final CardColorEnum diagonalCardColor;

    /**
     * The number of points that the player gets for each bottom right L shapes of the specified color in their card matrix
     */
    private final int pointsPerPattern;

    /**
     * Constructor of the class
     * @param columnCardsColor the color of the 2 cards that form the column of the L shape
     * @param diagonalCardColor the color of the card that's at the bottom right of the L shape
     * @param pointsPerPattern the number of points that the player gets for each bottom right L shapes
     *                         of the specified color in their card matrix
     */
    public BottomRightLShape(CardColorEnum columnCardsColor, CardColorEnum diagonalCardColor, int pointsPerPattern) {
        this.columnCardsColor = columnCardsColor;
        this.diagonalCardColor = diagonalCardColor;
        this.pointsPerPattern = pointsPerPattern;
    }

    /**
     * This method calculates the points of the player based on the number of bottom right L shapes
     *    of the specified color in their card matrix
     * @param player the player whose points are being calculated
     * @return the points of the player based on the number of bottom right L shapes
     *      of the specified color in their card matrix
     */
    public int calculatePoints(Player player) {
        ResourceCard[][] cards = player.getCards();
        ArrayList<ResourceCard> scoringCards = new ArrayList<>();
        int points = 0;

        for (int j = 0; j < 80; j++) {
            for (int i = 0 ; i < 79; i++) {
                if (cards[i][j] != null && !(cards[i][j] instanceof StarterCard)) {
                    if (cards[i][j].getColor() == columnCardsColor && !scoringCards.contains(cards[i][j])) {
                        if (cards[i + 2][j] != null && !(cards[i + 2][j] instanceof StarterCard) && cards[i + 2][j].getColor() == columnCardsColor && !scoringCards.contains(cards[i + 2][j])) {
                            if (cards[i + 3][j + 1] != null && !(cards[i + 3][j + 1] instanceof StarterCard) && cards[i + 3][j + 1].getColor() == diagonalCardColor && !scoringCards.contains(cards[i + 3][j + 1])) {
                                points = points + pointsPerPattern;

                                scoringCards.add(cards[i][j]);
                                scoringCards.add(cards[i + 2][j]);
                                scoringCards.add(cards[i + 3][j + 1]);

                            }
                        }
                    }
                }
            }
        }
        return points;
    }

    /**
     * This method returns the number of points that the player gets for each bottom right L shapes
     *      of the specified color in their card matrix
     * @return the number of points that the player gets for each bottom right L shapes
     *      of the specified color in their card matrix
     */
    public int getPointsPerPattern() {
        return pointsPerPattern;
    }
}
