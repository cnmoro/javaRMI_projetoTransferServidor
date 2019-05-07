package serventes;

import interfaces.InterfaceCli;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.InterfaceServCli;
import servidor.DbManager;

/**
 *
 * @author cnmoro
 */
public class ServenteServerCli extends UnicastRemoteObject implements InterfaceServCli {

    public ServenteServerCli() throws RemoteException {
    }

    @Override
    public void exibirCotacoes(InterfaceCli cliente) throws RemoteException {
        cliente.receberCotacoes(DbManager.getTransfers());
    }
}
