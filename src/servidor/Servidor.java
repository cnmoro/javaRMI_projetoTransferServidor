package servidor;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
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
            
            DbManager.adicionaTransfer(new TransferModel(
                    1,
                    125,
                    2,
                    "Van",
                    new Date(),
                    "Av Sete de Setembro, 3165 -> Av Silva Jardim, 500 -> Shopping Estação"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
