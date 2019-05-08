package serventes;

import interfaces.InterfaceCli;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.InterfaceServCli;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import servidor.DbManager;
import servidor.Notificacao;
import servidor.TransferModel;

/**
 *
 * @author cnmoro
 */
public class ServenteServerCli extends UnicastRemoteObject implements InterfaceServCli {

    public static HashMap<Integer, ArrayList<InterfaceCli>> mapaInteresseClientes = new HashMap<>();

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
            cliente.receberConfirmacaoReserva("Transfer indisponível ou já reservado");
        } else {
            //Registra o interesse do cliente no transfer e envia para o cliente
            adicionaInteresseCliente(id, cliente);
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
            DbManager.alteraTransfer(tm, "foi reservado");
            cliente.receberConfirmacaoReserva("Transfer " + tm.getId() + " reservado com sucesso");
        }
    }

    public static void enviaNotificacaoInteresse(int id, String modificação) throws RemoteException {
        //Encontrar clientes e motoristas que tem interesse neste transfer
        ArrayList<InterfaceCli> clientes = getClientesInteressadosEm(id);

        //Se houver pessoas interessadas neste transfer
        if (clientes != null) {
            //Enviar notificacao para cada um deles
            for (InterfaceCli cli : clientes) {
                cli.receberNotificacao("Transfer número " + id + " " + modificação);
            }
        }
    }

    public static ArrayList<InterfaceCli> getClientesInteressadosEm(int interesse) {
        //buscar no map a partir do interesse
        System.out.println("interesse: " + interesse);
        System.out.println("mapainteresseclientes keyset: " + mapaInteresseClientes.keySet());
        System.out.println("mapainteresseclientes get: " + mapaInteresseClientes.get(interesse));
        return mapaInteresseClientes.get(interesse);
    }

    public static synchronized void adicionaInteresseCliente(int interesse, InterfaceCli cliente) {
        //verificar se ja existe alguem com este interesse
        if (!mapaInteresseClientes.containsKey(interesse)) {
            //se nao existir, insere (cria nova lista e insere)
            ArrayList<InterfaceCli> clientes = new ArrayList<>();
            clientes.add(cliente);
            mapaInteresseClientes.put(interesse, clientes);
        } else {
            //se ja existir, pega a lista existente do mapeamento e insere o cliente como interessado
            ArrayList<InterfaceCli> clientes = mapaInteresseClientes.get(interesse);
            clientes.add(cliente);
            //substitui/atualiza a lista de interessados
            mapaInteresseClientes.replace(interesse, clientes);
        }

    }

    @Override
    public void startNotificacoes() {
        //Inicia task
        Timer timer = new Timer();
        timer.schedule(new VerificadorNotificacoes(), 0, 5000);
    }

    class VerificadorNotificacoes extends TimerTask {

        public void run() {
            //Verifica se existem novas
            //Se a fila não estiver vazia
            if (!DbManager.filaNotificacoesClientes.isEmpty()) {
                try {
                    enviaNotificacoes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public synchronized void enviaNotificacoes() throws RemoteException {
            Notificacao n = DbManager.filaNotificacoesClientes.remove();

            //Envia notificação para os interessados
            enviaNotificacaoInteresse(n.getId(), n.getModificacao());
        }
    }

}
