package serventes;

import interfaces.InterfaceServ;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import servidor.DbManager;

/**
 *
 * @author cnmoro
 */
public class ServenteServer extends UnicastRemoteObject implements InterfaceServ {

    public ServenteServer() throws RemoteException {
    }

    public String obterCotacao() {
        return null;
    }

    public String cadastrarTransfer() throws RemoteException {
        return null;
    }

    public String alterarTransfer() throws RemoteException {
        return null;
    }
}
