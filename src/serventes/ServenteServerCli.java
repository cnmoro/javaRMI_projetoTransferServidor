package serventes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.InterfaceServCli;

/**
 *
 * @author cnmoro
 */
public class ServenteServerCli extends UnicastRemoteObject implements InterfaceServCli {

    public ServenteServerCli() throws RemoteException {
    }

    public String obterCotacao() {
        return null;
    }

    public String reservarTransfer() throws RemoteException {
        return null;
    }
}
