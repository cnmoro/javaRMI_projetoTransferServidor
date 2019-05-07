package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author cnmoro
 */
public interface InterfaceServMotorista extends Remote {

    public String cadastrarTransfer() throws RemoteException;
//    Inserir

    public String alterarTransfer() throws RemoteException;
//    Fornecendo o tipo de veículo, número máximo
//de passageiros que o veículo suporta e preço do transfer) e uma
//proposta (redução de preço de transfer para um cliente específico)
}
