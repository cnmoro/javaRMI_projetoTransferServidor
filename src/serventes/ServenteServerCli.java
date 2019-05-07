package serventes;

import interfaces.InterfaceCli;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.InterfaceServCli;
import servidor.DbManager;
import servidor.TransferModel;

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
    
    @Override
    public void reservarTransfer(int id, InterfaceCli cliente) throws RemoteException {
        TransferModel tm = DbManager.getTransferPorId(id);
        if (tm == null || tm.isReservado()) {
            cliente.receberConfirmacaoReserva("Não é possível reservar este transfer");
        } else {
            tm.setReservado(true);
            DbManager.alteraTransfer(tm);
            cliente.receberConfirmacaoReserva("Transfer reservado com sucesso");
        }
    }
}
