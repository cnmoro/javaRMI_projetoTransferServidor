package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author cnmoro
 */
public interface InterfaceServCli extends Remote {

    public String obterCotacao() throws RemoteException;
//    Obter cotações e reserva de transfers

    public String reservarTransfer() throws RemoteException;
//    Fazer a reserva de um transfer
}
