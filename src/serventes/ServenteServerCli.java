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
    public void exibirTransfers(InterfaceCli cliente) throws RemoteException {
        //Envia lista de transfers
        cliente.receberListagemTransfers(DbManager.getTransfers());
    }

    @Override
    public void realizarCotacao(int id, InterfaceCli cliente) throws RemoteException {
        //Obtem o transfer com id requerido
        TransferModel tm = DbManager.getTransferPorId(id);
        //Verifica se existe ou se já está reservado
        if (tm == null || tm.isReservado()) {
            cliente.receberConfirmacaoReserva("transfer indisponível ou já reservado");
        } else {
            //Registra o interesse do cliente no transfer e envia para o cliente
            DbManager.adicionaInteresseCliente(id, cliente);
            cliente.receberCotacao(DbManager.getCotacao(tm));
        }
    }

    @Override
    public void reservarTransfer(int id, InterfaceCli cliente) throws RemoteException {
        //Obtem o transfer com id requerido
        TransferModel tm = DbManager.getTransferPorId(id);
        //Verifica se existe ou se já está reservado
        if (tm == null || tm.isReservado()) {
            cliente.receberConfirmacaoReserva("Transfer indisponível ou já reservado");
        } else {
            //Marca o transfer como reservado e informa o cliente
            tm.setReservado(true);
            DbManager.alteraTransfer(tm, "o transfer de número " + tm.getId() + " foi reservado por um cliente", "cliente");
            cliente.receberConfirmacaoReserva("transfer " + tm.getId() + " reservado com sucesso");
        }
    }

}
