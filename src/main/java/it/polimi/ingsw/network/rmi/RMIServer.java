package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.ServerNetworkHandler;
import it.polimi.ingsw.network.packets.Packet;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

    private final ServerNetworkHandler networkHandler;

    public RMIServer(ServerNetworkHandler networkHandler, String registryName, int port) throws RemoteException {
        super();
        this.networkHandler = networkHandler;
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(registryName, this);
    }

    public void receivePacket(Packet packet, RMIClientInterface clientInterface) {
        RMIClientConnection connection = new RMIClientConnection(clientInterface);
        networkHandler.addConnection(connection);
        networkHandler.receivePacket(packet, connection);
    }

    public void login(RMIClientInterface clientInterface){
        RMIClientConnection connection = new RMIClientConnection(clientInterface);
        networkHandler.addConnection(connection);
    }

}
