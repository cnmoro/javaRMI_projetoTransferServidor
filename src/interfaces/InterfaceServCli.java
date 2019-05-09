package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author cnmoro
 */
public interface InterfaceServCli extends Remote {

    //Obter transfers
    public void exibirTransfers(InterfaceCli cliente) throws RemoteException;

    //Obter cotacao
    public void realizarCotacao(int id, InterfaceCli cliente) throws RemoteException;

    //Reservar transfer
    public void reservarTransfer(int id, InterfaceCli cliente) throws RemoteException;
}
