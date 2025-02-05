package it.polimi.ingsw.client.controller.packethandlers;

import it.polimi.ingsw.client.controller.ClientManager;
import it.polimi.ingsw.client.controller.Printer;
import it.polimi.ingsw.client.controller.clientstate.ClientStatusEnum;
import it.polimi.ingsw.client.view.UserInterface;
import it.polimi.ingsw.client.view.ViewModeEnum;
import it.polimi.ingsw.client.view.gui.GUIClient;
import it.polimi.ingsw.network.packets.ChooseStarterFacePacket;
import it.polimi.ingsw.network.packets.Packet;
import it.polimi.ingsw.server.model.card.FaceEnum;
import it.polimi.ingsw.server.model.card.StarterCard;
import it.polimi.ingsw.server.model.card.corner.Corner;
import it.polimi.ingsw.server.model.resources.Resource;

/**
 * The class that handles the starter card face choosing packets from the server
 */
public class ClientChooseStarterFacePacketHandler extends ClientPacketHandler {

    /**
     * The method handles the starter card face choosing packet
     *
     * @param packet        the choose starter card face packet
     * @param clientManager the client manager
     */
    @Override
    public void handlePacket(Packet packet, ClientManager clientManager) {
        ChooseStarterFacePacket chooseStarterFacePacket = (ChooseStarterFacePacket) packet;
        StarterCard card = (StarterCard) clientManager.getGameState().getCardById(chooseStarterFacePacket.getStarterID());
        if (!chooseStarterFacePacket.getUsername().equals(clientManager.getGameState().getUsername())) {
            if (chooseStarterFacePacket.getChosenStarterFace() != null) {
                card.setFace(chooseStarterFacePacket.getChosenStarterFace());
            }
            clientManager.getGameState().getOrCreatePlayerStateByNick(chooseStarterFacePacket.getUsername()).setStarterCard(card);
            return;
        }

        if (chooseStarterFacePacket.getChosenStarterFace() != null) {
            card.setFace(chooseStarterFacePacket.getChosenStarterFace());
            UserInterface userInterface = clientManager.getUserInterface();
            userInterface.showMessage(Printer.GREEN + "You have chosen your Starter Card face." + Printer.RESET);
            clientManager.getGameState().setStarterCard(card);
            if (clientManager.getViewMode() == ViewModeEnum.GUI) {
                GUIClient guiClient = (GUIClient) userInterface;
                guiClient.updateCurrentScene(null);
            }
            for (Corner corner : card.getCorners()) {
                if (corner.getFace().equals(card.getFace())) {
                    clientManager.getGameState().addResource(corner.getResource().getType().name());
                }
            }
            if (card.getFace().equals(FaceEnum.BACK)) {
                for (Resource res : card.getBackResources()) {
                    clientManager.getGameState().addResource(res.getType().name());
                }
            }
        } else {
            clientManager.getGameState().setClientStatus(ClientStatusEnum.CHOOSING_STARTER_FACE);
            clientManager.getGameState().setGivenStarter(card);
            if (ClientManager.getInstance().getViewMode() == ViewModeEnum.GUI) {
                ((GUIClient) ClientManager.getInstance().getUserInterface()).changeScene(ClientManager.getInstance().getGameState().getClientStatus());
            } else {
                System.out.println(Printer.CYAN + "Choose one of the following faces with /chooseStarterFace <1/2>:" + Printer.RESET);
                card.setFace(FaceEnum.FRONT);
                Printer.printCard(card);
                card.setFace(FaceEnum.BACK);
                Printer.printCard(card);
                card.setFace(FaceEnum.FRONT);
            }
        }
    }
}
