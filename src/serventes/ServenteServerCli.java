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

    InterfaceCli cliente = null;

    public ServenteServerCli() throws RemoteException {
    }

    public void bindInterfaceCliente(InterfaceCli ic) {
        if (this.cliente == null) {
            this.cliente = ic;
        }
    }

    @Override
    public void exibirTransfers(InterfaceCli cliente) throws RemoteException {
        bindInterfaceCliente(cliente);

        //Envia lista de transfers
        cliente.receberListagemTransfers(DbManager.getTransfers());
    }

    @Override
    public void realizarCotacao(int id, InterfaceCli cliente) throws RemoteException {
        bindInterfaceCliente(cliente);

        //Obtem o transfer com id requerido
        TransferModel tm = DbManager.getTransferPorId(id);
        //Verifica se existe ou se já está reservado
        if (tm == null || tm.isReservado()) {
            cliente.receberConfirmacaoReserva("Transfer indisponível ou já reservado");
        } else {
            //Registra o interesse do cliente no transfer e envia para o cliente
            DbManager.adicionaInteresseCliente(id, cliente);
            cliente.receberCotacao(DbManager.getCotacao(tm));
        }
    }

    @Override
    public void reservarTransfer(int id, InterfaceCli cliente) throws RemoteException {
        bindInterfaceCliente(cliente);

        //Obtem o transfer com id requerido
        TransferModel tm = DbManager.getTransferPorId(id);
        //Verifica se existe ou se já está reservado
        if (tm == null || tm.isReservado()) {
            cliente.receberConfirmacaoReserva("Transfer indisponível ou já reservado");
        } else {
            //Marca o transfer como reservado e informa o cliente
            tm.setReservado(true);
            DbManager.alteraTransfer(tm);
            cliente.receberConfirmacaoReserva("Transfer " + tm.getId() + " reservado com sucesso");
        }
    }
    
}
