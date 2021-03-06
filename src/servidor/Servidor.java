package servidor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import serventes.ServenteServerCli;
import serventes.ServenteServerMotorista;

/**
 *
 * @author cnmoro
 */
public class Servidor {

    public static void main(String[] args) throws RemoteException {
        try {
            Registry servicoNomes = LocateRegistry.createRegistry(1099);

            //Ref para o cliente
            ServenteServerCli serventeServidorCli = new ServenteServerCli();

            //Ref para o motorista
            ServenteServerMotorista serventeServidorMot = new ServenteServerMotorista();

            //Vincula a ref para o cliente no servico de nomes
            servicoNomes.bind("ReferenciaServenteServidorCli", serventeServidorCli);

            //Vincula a ref para o motorista no servico de nomes
            servicoNomes.bind("ReferenciaServenteServidorMot", serventeServidorMot);

            System.out.println("Server Rodando");

            //Inicia a tarefa de checagem de novas notificações para enviar aos clientes
            DbManager.startNotificacoesClientes();
            DbManager.startNotificacoesMotoristas();

            /*
            TransferModel tm1 = new TransferModel(
                    1,
                    125,
                    2,
                    "Van",
                    new Date(),
                    "Av Sete de Setembro, 3165 - Av Silva Jardim, 500 - Shopping Estação",
                    false
            );

            TransferModel tm2 = new TransferModel(
                    2,
                    105,
                    1,
                    "Carro comum",
                    new Date(),
                    "Av Iguaçu -> Shopping Estação - Shopping Curitiba",
                    false
            );
             */
            //DbManager.adicionaTransfer(tm1);
            //DbManager.adicionaTransfer(tm2);
            //TimeUnit.SECONDS.sleep(10);
            //tm2.setPreco(100);
            //System.out.println("Alterando preço de transfer 2...");
            //DbManager.alteraTransfer(tm2, "teve seu preço reduzido");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
