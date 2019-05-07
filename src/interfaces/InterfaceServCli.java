package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author cnmoro
 */
public interface InterfaceServCli extends Remote {

    //Obter cotações
    public void exibirCotacoes(InterfaceCli cliente) throws RemoteException;
}
