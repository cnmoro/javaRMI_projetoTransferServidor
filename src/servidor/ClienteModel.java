package servidor;

import interfaces.InterfaceCli;

/**
 *
 * @author cnmoro
 */
public class ClienteModel {

    InterfaceCli clienteRef;
    int clienteId;

    public ClienteModel() {
    }

    public ClienteModel(InterfaceCli clienteRef, int clienteId) {
        this.clienteRef = clienteRef;
        this.clienteId = clienteId;
    }

    public InterfaceCli getClienteRef() {
        return clienteRef;
    }

    public void setClienteRef(InterfaceCli clienteRef) {
        this.clienteRef = clienteRef;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

}
