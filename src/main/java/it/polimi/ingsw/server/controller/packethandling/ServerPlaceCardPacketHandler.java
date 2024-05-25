package it.polimi.ingsw.server.controller.packethandling;

import it.polimi.ingsw.client.controller.Printer;
import it.polimi.ingsw.network.ClientConnection;
import it.polimi.ingsw.network.packets.InfoPacket;
import it.polimi.ingsw.network.packets.Packet;
import it.polimi.ingsw.network.packets.PlaceCardPacket;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.PlayerController;
import it.polimi.ingsw.server.controller.exceptions.IllegalCardPlacementException;
import it.polimi.ingsw.server.model.card.ResourceCard;
import it.polimi.ingsw.server.model.resources.Object;
import it.polimi.ingsw.server.model.resources.ObjectTypeEnum;
import it.polimi.ingsw.server.model.resources.Resource;
import it.polimi.ingsw.server.model.resources.ResourceTypeEnum;

import java.util.HashMap;

public class ServerPlaceCardPacketHandler extends ServerPacketHandler {

    @Override
    public void handlePacket(Packet packet, GameController controller, ClientConnection clientConnection) {
        PlaceCardPacket placeCardPacket = (PlaceCardPacket) packet;
        if (!controller.getGame().getInfo().getActivePlayer().getUsername().equals(clientConnection.getUsername()) || controller.getPlayerController(clientConnection.getUsername()).getPlayer().getCardsInHand().size() != 3) {
            controller.getNetworkHandler().sendPacket(clientConnection, new InfoPacket(Printer.RED + "You can't place a Card now." + Printer.RESET));
            return;
        }
        try {
            ResourceCard card = (ResourceCard) controller.getCardById(placeCardPacket.getCardId());
            PlayerController playerController = controller.getPlayerController(clientConnection.getUsername());
            if (playerController.getPlayer().getCardsInHand().contains(card)) {
                playerController.placeCard(card, placeCardPacket.getXCoord(), placeCardPacket.getYCoord());
                playerController.getPlayer().removeCardInHand(card);
                HashMap<String, Integer> resources = new HashMap<>();
                for (ResourceTypeEnum res: ResourceTypeEnum.values()) {
                    resources.put(res.name(), 0);
                }
                for (Resource resource : playerController.getPlayer().getResources()) {
                    resources.put(resource.getType().name(), resources.get(resource.getType().name()) + 1);
                }

                for (ObjectTypeEnum obj: ObjectTypeEnum.values()) {
                    resources.put(obj.name(), 0);
                }
                for (Object object : playerController.getPlayer().getObjects()) {
                    resources.put(object.getType().name(), resources.get(object.getType().name()) + 1);
                }
                controller.getNetworkHandler().sendPacketToAll(new PlaceCardPacket(clientConnection.getUsername(), playerController.getPlayer().getScore(), resources, placeCardPacket.getCardId(), placeCardPacket.getXCoord(), placeCardPacket.getYCoord()));
            } else {
                controller.getNetworkHandler().sendPacket(clientConnection, new InfoPacket(Printer.RED + "You don't have that Card." + Printer.RESET));
            }
        } catch (IllegalCardPlacementException ex) {
            controller.getNetworkHandler().sendPacket(clientConnection, new InfoPacket(Printer.RED + "You can't place that Card here." + Printer.RESET));
        }
    }
}
