package it.polimi.ingsw.network.packets;

import it.polimi.ingsw.client.controller.packethandlers.ClientPacketHandler;
import it.polimi.ingsw.server.controller.packethandling.ServerPacketHandler;

public class EndTurnPacket extends Packet {


    @Override
    public ClientPacketHandler getClientPacketHandler() {
        return null;
    }

    @Override
    public ServerPacketHandler getServerPacketHandler() {
        return null;
    }
}
