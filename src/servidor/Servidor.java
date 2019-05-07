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

            ServenteServerCli serventeServidorCli = new ServenteServerCli();
            servicoNomes.bind("ReferenciaServenteServidorCli", serventeServidorCli);

            ServenteServerMotorista serventeServidorMoto = new ServenteServerMotorista();
            servicoNomes.bind("ReferenciaServenteServidorMoto", serventeServidorMoto);

            System.out.println("Server Rodando");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
