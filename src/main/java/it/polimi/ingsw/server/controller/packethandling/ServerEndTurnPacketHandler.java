package it.polimi.ingsw.server.controller.packethandling;

import it.polimi.ingsw.client.controller.Printer;
import it.polimi.ingsw.network.ClientConnection;
import it.polimi.ingsw.network.packets.EndTurnPacket;
import it.polimi.ingsw.network.packets.InfoPacket;
import it.polimi.ingsw.network.packets.Packet;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.game.GameStatusEnum;

public class ServerEndTurnPacketHandler extends ServerPacketHandler {

    @Override
    public void handlePacket(Packet packet, GameController controller, ClientConnection clientConnection) {
        EndTurnPacket endTurnPacket = (EndTurnPacket) packet;
        if (controller.getGame().getInfo().getActivePlayer().getUsername().equals(endTurnPacket.getActivePlayer())) {
            if (controller.getGame().getInfo().getGameStatus() == GameStatusEnum.WAITING_FOR_PLAYERS && controller.getGame().getInfo().getPlayersNumber() == 1) {
                controller.getNetworkHandler().sendPacket(clientConnection, new InfoPacket(Printer.RED + "You are the only Player connected, wait for someone else to connect." + Printer.RESET));
                return;
            }
            controller.nextTurn();
        } else {
            controller.getNetworkHandler().sendPacket(clientConnection, new InfoPacket("It's not your turn!"));
        }
    }
}
