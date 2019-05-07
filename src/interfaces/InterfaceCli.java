package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author cnmoro
 */
public interface InterfaceCli extends Remote {

    public void receberNotificacao(String interesse) throws RemoteException;

    public void adicionarInteresse(String interesse) throws RemoteException;

    public void reservarTransfer(int id) throws RemoteException;

    public void receberCotacoes(String cotacoes) throws RemoteException;
}
