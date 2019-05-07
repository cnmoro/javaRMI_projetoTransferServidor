package servidor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import serventes.ServenteServerCli;

/**
 *
 * @author cnmoro
 */
public class Servidor {

    public static void main(String[] args) throws RemoteException {
        try {
            Registry servicoNomes = LocateRegistry.createRegistry(1099);

            ServenteServerCli serventeServidorCli = new ServenteServerCli();

            servicoNomes.bind("ReferenciaServenteServidorCli", serventeServidorCli);

            System.out.println("Server Rodando");

            TransferModel tm1 = new TransferModel(
                    1,
                    125,
                    2,
                    "Van",
                    new Date(),
                    "Av Sete de Setembro, 3165 -> Av Silva Jardim, 500 -> Shopping Estação",
                    false
            );

            TransferModel tm2 = new TransferModel(
                    2,
                    105,
                    1,
                    "Carro comum",
                    new Date(),
                    "Av Iguaçu -> Shopping Estação -> Shopping Curitiba",
                    false
            );

            DbManager.adicionaTransfer(tm1);
            DbManager.adicionaTransfer(tm2);

            TimeUnit.SECONDS.sleep(6);

            tm2.setNumPassageiros(3);
            DbManager.alteraTransfer(tm2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
