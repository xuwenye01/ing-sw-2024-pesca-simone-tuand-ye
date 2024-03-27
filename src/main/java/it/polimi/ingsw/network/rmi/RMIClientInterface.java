package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.packets.ClientPacket;
import it.polimi.ingsw.network.packets.Packet;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientInterface extends Remote {
    void sendPacket(ClientPacket packet) throws RemoteException;

    void receivePacket(Packet packet) throws RemoteException;

}