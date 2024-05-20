package it.polimi.ingsw.client.controller.packethandlers;

import it.polimi.ingsw.client.controller.ClientManager;
import it.polimi.ingsw.client.controller.Printer;
import it.polimi.ingsw.client.controller.clientstate.ClientStatusEnum;
import it.polimi.ingsw.client.controller.clientstate.PlayerState;
import it.polimi.ingsw.network.packets.ChooseColorPacket;
import it.polimi.ingsw.network.packets.Packet;

public class ClientChooseColorPacketHandler extends ClientPacketHandler {

    @Override
    public void handlePacket(Packet packet, ClientManager clientManager) {
        ChooseColorPacket chooseColorPacket = (ChooseColorPacket) packet;
        System.out.println(Printer.GREEN + chooseColorPacket.getUsername() + " has chosen the color " + chooseColorPacket.getColor() + Printer.RESET);
        if (chooseColorPacket.getUsername().equals(clientManager.getGameState().getUsername())) {
            clientManager.getGameState().setColor(chooseColorPacket.getColor());
            clientManager.getGameState().setClientStatus(ClientStatusEnum.PLAYING);
        } else {
            for (PlayerState playerState : clientManager.getGameState().getPlayerStates()) {
                if (playerState.getUsername().equals(chooseColorPacket.getUsername())) {
                    playerState.setColor(chooseColorPacket.getColor());
                }
            }
        }
    }
}