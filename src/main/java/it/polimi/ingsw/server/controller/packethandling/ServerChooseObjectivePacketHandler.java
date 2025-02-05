package it.polimi.ingsw.server.controller.packethandling;

import it.polimi.ingsw.client.controller.Printer;
import it.polimi.ingsw.network.ClientConnection;
import it.polimi.ingsw.network.packets.ChooseObjectivePacket;
import it.polimi.ingsw.network.packets.InfoPacket;
import it.polimi.ingsw.network.packets.Packet;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.card.ObjectiveCard;
import it.polimi.ingsw.server.model.game.GameStatusEnum;

/**
 * The class that handles the objective card choosing packets from the clients
 */
public class ServerChooseObjectivePacketHandler extends ServerPacketHandler {

    /**
     * The method handles the objective card choosing packets from the client
     * @param packet the choose objective card packet
     * @param controller the game controller
     * @param clientConnection the connection of the client
     */
    @Override
    public void handlePacket(Packet packet, GameController controller, ClientConnection clientConnection) {
        ChooseObjectivePacket chooseObjectivePacket = (ChooseObjectivePacket) packet;
        if (controller.getGame().getInfo().getGameStatus() != GameStatusEnum.CHOOSING_PERSONAL_OBJECTIVE) {
            controller.getNetworkHandler().sendPacket(clientConnection, new InfoPacket(Printer.RED + "You can't choose your Objective Card now." + Printer.RESET));
            return;
        }
        int chosenCardID = chooseObjectivePacket.getChosenCardID();
        if (chosenCardID > 0) {
            ObjectiveCard card = (ObjectiveCard) controller.getCardById(chosenCardID);
            controller.getPlayerController(clientConnection.getUsername()).chooseObjectiveCard(card);
            controller.getNetworkHandler().sendPacket(clientConnection, chooseObjectivePacket);
            controller.checkPreGameConditions();
        }
    }
}
