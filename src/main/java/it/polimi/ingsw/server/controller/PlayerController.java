package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.exceptions.AlreadyTakenColorException;
import it.polimi.ingsw.server.controller.exceptions.IllegalCardPlacementException;
import it.polimi.ingsw.server.model.card.*;
import it.polimi.ingsw.server.model.card.corner.Corner;
import it.polimi.ingsw.server.model.card.corner.CornerLocationEnum;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.PlayerToken;
import it.polimi.ingsw.server.model.player.TokenColorEnum;
import it.polimi.ingsw.server.model.resources.ObjectTypeEnum;
import it.polimi.ingsw.server.model.resources.Resource;
import it.polimi.ingsw.server.model.resources.ResourceTypeEnum;

public record PlayerController(Player player) {

    public Player getPlayer() {
        return player;
    }

    public synchronized void placeCard(ResourceCard card, int x, int y) throws IllegalCardPlacementException {
        if (canPlaceCard(x, y, card)) {
            player.setCard(card, x, y);

            addResourcesAndObjects(card);

            ResourceCard currCard;
            CornerLocationEnum currRemovingCornerLocation = CornerLocationEnum.values()[0];
            //TopLeftCard -> TopRightCard -> BottomLeftCard -> BottomRightCard
            for (int i = -1; i <= 1; i += 2) {
                for (int j = -1; j <= 1; j += 2) {
                    if (player.getCardAt(x + j, y + i).isPresent()) {

                        currCard = player.getCardAt(x + j, y + i).get();

                        CornerLocationEnum finalRemovingCornerLocation = currRemovingCornerLocation;
                        ResourceCard finalCurrCard = currCard;

                        player.getCardAt(x + j, y + i).get().getCorners().stream()
                                .filter(corner -> corner.getLocation().equals(finalRemovingCornerLocation))
                                .filter(corner -> corner.getFace().equals(finalCurrCard.getFace()))
                                .findFirst().ifPresent(corner -> player.removeObject(corner.getObject()));

                        player.getCardAt(x + j, y + i).get().getCorners().stream()
                                .filter(corner -> corner.getLocation().equals(finalRemovingCornerLocation))
                                .filter(corner -> corner.getFace().equals(finalCurrCard.getFace()))
                                .findFirst().ifPresent(corner -> player.removeResource(corner.getResource()));

                    }
                    if (currRemovingCornerLocation.ordinal() < 3)
                        currRemovingCornerLocation = CornerLocationEnum.values()[currRemovingCornerLocation.ordinal() + 1];
                }
            }

            if (card instanceof GoldCard goldCard && card.getFace().equals(FaceEnum.FRONT)) {
                player.setScore(player.getScore() + goldCard.getPointsStrategy().getStrategy().calculatePoints(player, x, y));
            } else if (card.getFace().equals(FaceEnum.FRONT)) {
                player.setScore(player.getScore() + card.getPoints());
            }
        } else {
            throw new IllegalCardPlacementException();
        }
    }

    public synchronized boolean canPlaceCard(int x, int y, ResourceCard card) {

        if ((card instanceof GoldCard goldCard) && card.getFace().equals(FaceEnum.FRONT)) {
            if (!goldCard.meetRequirements(player.getResources())) {
                return false;
            }
        }

        if (player.getCardAt(x, y).isEmpty()) {
            boolean isPlaceable = false;

            if (player.getCardAt(x - 1, y - 1).isPresent()) {
                isPlaceable = true;
                ResourceCard neighbour = player.getCardAt(x - 1, y - 1).get();
                if (!neighbour.getCorner(CornerLocationEnum.BOTTOM_RIGHT).isVisible()) return false;
            }
            if (player.getCardAt(x + 1, y + 1).isPresent()) {
                isPlaceable = true;
                ResourceCard neighbour = player.getCardAt(x + 1, y + 1).get();
                if (!neighbour.getCorner(CornerLocationEnum.TOP_LEFT).isVisible()) return false;
            }
            if (player.getCardAt(x - 1, y + 1).isPresent()) {
                isPlaceable = true;
                ResourceCard neighbour = player.getCardAt(x - 1, y + 1).get();
                if (!neighbour.getCorner(CornerLocationEnum.BOTTOM_LEFT).isVisible()) return false;
            }
            if (player.getCardAt(x + 1, y - 1).isPresent()) {
                isPlaceable = true;
                ResourceCard neighbour = player.getCardAt(x + 1, y - 1).get();
                if (!neighbour.getCorner(CornerLocationEnum.TOP_RIGHT).isVisible()) return false;
            }
            return isPlaceable;
        }
        return false;
    }

    public synchronized void addResourcesAndObjects(ResourceCard card) {
        if (card.getFace().equals(FaceEnum.BACK)) {
            for (Resource res : card.getBackResources()) {
                player.addResource(res);
            }
        }

        for (Corner corner : card.getCorners()) {
            if (corner.isVisible() && corner.getResource() != null && corner.getResource().getType() != ResourceTypeEnum.EMPTY && corner.getFace() == card.getFace()) {
                player.addResource(corner.getResource());
            }
            if (corner.isVisible() && corner.getObject() != null && corner.getObject().getType() != ObjectTypeEnum.EMPTY && corner.getFace() == card.getFace()) {
                player.addObject(corner.getObject());
            }
        }
    }

    public synchronized void chooseToken(TokenColorEnum tokenColor) {
        for (Player player : player.getGame().getPlayers()) {
            if (player.getToken() != null && player.getToken().color().equals(tokenColor)) {
                throw new AlreadyTakenColorException(tokenColor);
            }
        }
        player.setToken(new PlayerToken(tokenColor));
    }

    public synchronized void chooseObjectiveCard(ObjectiveCard objectiveCard) {
        player.setObjectiveCard(objectiveCard);
    }

    public synchronized void setStarterCard(StarterCard starterCard, FaceEnum chosenFace) {
        starterCard.setFace(chosenFace);
        player.setStarterCard(starterCard);
        player.setCard(starterCard, 40, 40);
        addResourcesAndObjects(starterCard);
    }

    public synchronized void turnCard(Card card) {
        if (card.getFace() == FaceEnum.FRONT) {
            card.setFace(FaceEnum.BACK);
        } else {
            card.setFace(FaceEnum.FRONT);
        }
    }
}
