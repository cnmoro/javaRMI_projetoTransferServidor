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

    public void receberConfirmacaoReserva(String mensagem) throws RemoteException;

    public void receberCotacoes(String cotacoes) throws RemoteException;
}
