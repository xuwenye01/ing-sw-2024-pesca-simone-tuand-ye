package it.polimi.ingsw.client.controller.packethandlers;

import it.polimi.ingsw.client.controller.ClientManager;
import it.polimi.ingsw.client.controller.Printer;
import it.polimi.ingsw.client.controller.clientstate.ClientStatusEnum;
import it.polimi.ingsw.network.packets.ChooseStarterFacePacket;
import it.polimi.ingsw.network.packets.Packet;
import it.polimi.ingsw.server.model.card.FaceEnum;
import it.polimi.ingsw.server.model.card.ObjectiveCard;
import it.polimi.ingsw.server.model.card.StarterCard;
import it.polimi.ingsw.server.model.card.corner.Corner;
import it.polimi.ingsw.server.model.resources.Resource;

public class ClientChooseStarterFacePacketHandler extends ClientPacketHandler{
    @Override
    public void handlePacket(Packet packet, ClientManager clientManager) {
        ChooseStarterFacePacket chooseStarterFacePacket = (ChooseStarterFacePacket) packet;

        if (chooseStarterFacePacket.getChosenStarterFace() != null) {
            StarterCard card = (StarterCard) clientManager.getGameState().getCardById(chooseStarterFacePacket.getStarterID());
            card.setFace(chooseStarterFacePacket.getChosenStarterFace());

            if (chooseStarterFacePacket.getUsername().equals(clientManager.getGameState().getUsername())) {
                System.out.println(Printer.GREEN + "You have chosen your Starter Card face." + Printer.RESET);
                clientManager.getGameState().setStarterCard(card);
                clientManager.getGameState().setClientStatus(ClientStatusEnum.CHOOSING_OBJECTIVE);
                for (Corner corner : card.getCorners()) {
                    if (corner.getFace().equals(chooseStarterFacePacket.getChosenStarterFace())) clientManager.getGameState().addResource(corner.getResource().getType().name());
                }
                if (card.getFace().equals(FaceEnum.BACK)) {
                    for (Resource res: card.getBackResources()) {
                        clientManager.getGameState().addResource(res.getType().name());
                    }
                }
            } else {
                clientManager.getGameState().getPlayerStateByNick(chooseStarterFacePacket.getUsername()).setStarterCard(card);
            }

        } else if (chooseStarterFacePacket.getUsername().equals(clientManager.getGameState().getUsername())){
            clientManager.getGameState().setClientStatus(ClientStatusEnum.CHOOSING_STARTER_FACE);
            StarterCard card = (StarterCard) clientManager.getGameState().getCardById(chooseStarterFacePacket.getStarterID());
            System.out.println(Printer.CYAN + "Choose one of the following faces with /chooseStarterFace <1/2>:" + Printer.RESET);
            card.setFace(FaceEnum.FRONT);
            Printer.printCard(card);
            card.setFace(FaceEnum.BACK);
            Printer.printCard(card);
            card.setFace(FaceEnum.FRONT);
            clientManager.getGameState().setGivenStarter(card);
        }
    }
}