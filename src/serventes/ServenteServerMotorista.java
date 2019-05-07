package serventes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.InterfaceServMotorista;

/**
 *
 * @author cnmoro
 */
public class ServenteServerMotorista extends UnicastRemoteObject implements InterfaceServMotorista {

    public ServenteServerMotorista() throws RemoteException {
    }

    public String cadastrarTransfer() {
        return null;
    }

    public String alterarTransfer() throws RemoteException {
        return null;
    }
}
