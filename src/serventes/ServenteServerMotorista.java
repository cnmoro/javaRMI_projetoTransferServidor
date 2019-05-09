package serventes;

import com.google.gson.Gson;
import interfaces.InterfaceMotorista;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.InterfaceServMotorista;
import servidor.DbManager;
import servidor.TransferModel;

/**
 *
 * @author cnmoro
 */
public class ServenteServerMotorista extends UnicastRemoteObject implements InterfaceServMotorista {

    public ServenteServerMotorista() throws RemoteException {
    }

    @Override
    public void cadastrarTransfer(String tm, InterfaceMotorista motorista) throws RemoteException {
        //Converte o json em TransferModel
        Gson gson = new Gson();
        TransferModel transfer = gson.fromJson(tm, TransferModel.class);

        //Obtem quantos transfers existem
        int quantidade = DbManager.transfers.size();
        //Seta o id do transfer como quantidade + 1, simulando auto increment do sql
        transfer.setId(quantidade + 1);
        //Adiciona o transfer
        DbManager.adicionaTransfer(transfer);
        //Informa o motorista que foi adicionado com sucesso
        motorista.receberConfirmacao("O transfer foi adicionado com sucesso." + gson.toJson(transfer));
        //Marca o motorista como interessado em sua oferta de transfer
        DbManager.adicionaInteresseMotorista(transfer.getId(), motorista);
    }

    @Override
    public void alterarTransfer(String tm, InterfaceMotorista motorista) throws RemoteException {
        //Converte o json em TransferModel
        Gson gson = new Gson();
        TransferModel transfer = gson.fromJson(tm, TransferModel.class);

        //Busca o transfer a ser alterado
        int index = DbManager.getTransferIndicePorId(transfer.getId());
        //Se o transfer existe
        if (index != -1) {
            //Realiza a modificacao
            //DbManager.transfers.get(index).change(transfer);
            DbManager.alteraTransfer(transfer, "foi atualizado pelo motorista", "motorista");
            //Avisa o motorista que deu tudo certo
            motorista.receberConfirmacao("O transfer foi atualizado com sucesso." + gson.toJson(transfer));
        } else {
            motorista.receberConfirmacao("Não foi possível encontrar este transfer para realizar alteração");
        }
    }

    @Override
    public void realizarProposta(int transferId, double novoPreco, int clienteId) throws RemoteException {
        TransferModel transf = DbManager.getTransferPorId(transferId);

        //Faz a rotina da proposta somente se o novo preco for menor
        if (novoPreco < transf.getPreco()) {
            System.out.println("Redirecionando proposta de novo preço do transfer de número " + transferId + " para o cliente " + clienteId);

            //Atualiza o preco
            transf.setPreco(novoPreco);
            DbManager.alteraTransfer(transf, "teve seu valor reduzido", "motorista");

            //Envia notificacao para o interessado
            DbManager.enviaNotificacaoProposta(transferId, novoPreco, clienteId);
        }
    }
}
