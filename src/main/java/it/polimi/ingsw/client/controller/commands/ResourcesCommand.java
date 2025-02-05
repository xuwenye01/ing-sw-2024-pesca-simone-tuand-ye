package it.polimi.ingsw.client.controller.commands;

import it.polimi.ingsw.client.controller.ClientManager;
import it.polimi.ingsw.client.controller.Printer;
import it.polimi.ingsw.client.controller.clientstate.ClientStatusEnum;
import it.polimi.ingsw.server.model.resources.ObjectTypeEnum;
import it.polimi.ingsw.server.model.resources.ResourceTypeEnum;

/**
 * The class represents the command that allows a player to view his Resources
 */
public class ResourcesCommand extends Command {

    /**
     * The constructor of the class
     */
    public ResourcesCommand() {
        commandName = "/resources";
        description = "  Shows your Resources or the ones of another Player" +
                " \n  Usage: /resources <player>";
        addValidStatus(ClientStatusEnum.PLAYING);
        addValidStatus(ClientStatusEnum.LAST_TURN);
    }

    /**
     * The method executes the command that allows a player to view his Resources
     * @param input the input of the command
     * @param clientManager the client manager
     */
    @Override
    public void executeCommand(String input, ClientManager clientManager) {
        if (isExecutable(clientManager)) {
            if (input.trim().isEmpty()) {
                System.out.println(Printer.GREEN + "Your Resources:");
                System.out.println(Printer.RED + clientManager.getGameState().getResources().getOrDefault(ResourceTypeEnum.FUNGI.name(), 0) + " Fungi");
                System.out.println(Printer.BLUE + clientManager.getGameState().getResources().getOrDefault(ResourceTypeEnum.ANIMAL.name(), 0) + " Animal");
                System.out.println(Printer.PURPLE + clientManager.getGameState().getResources().getOrDefault(ResourceTypeEnum.INSECT.name(), 0) + " Insect");
                System.out.println(Printer.GREEN + clientManager.getGameState().getResources().getOrDefault(ResourceTypeEnum.PLANT.name(), 0) + " Plant");
                System.out.println(Printer.YELLOW + clientManager.getGameState().getResources().getOrDefault(ObjectTypeEnum.INKWELL.name(), 0) + " Inkwell");
                System.out.println(Printer.YELLOW + clientManager.getGameState().getResources().getOrDefault(ObjectTypeEnum.MANUSCRIPT.name(), 0) + " Manuscript");
                System.out.println(Printer.YELLOW + clientManager.getGameState().getResources().getOrDefault(ObjectTypeEnum.QUILL.name(), 0) + " Quill" + Printer.RESET);

            } else {
                System.err.println("Usage: /resources <player>");
            }
        } else {
            System.err.println("You can't view your Resources now.");
        }
    }

    /**
     * The method checks if the command is executable
     * @param clientManager the client manager
     * @return true if the command is executable, false otherwise
     */
    public boolean isExecutable(ClientManager clientManager) {
        return getValidStatuses().contains(clientManager.getGameState().getClientStatus());
    }
}
