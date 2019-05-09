package servidor;

import interfaces.InterfaceCli;
import interfaces.InterfaceMotorista;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author cnmoro
 */
public class DbManager {

    public static ArrayList<TransferModel> transfers = new ArrayList<>();
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static Queue<Notificacao> filaNotificacoesClientes = new LinkedList<>();
    public static Queue<Notificacao> filaNotificacoesMotoristas = new LinkedList<>();

    public static HashMap<Integer, ArrayList<InterfaceMotorista>> mapaInteresseMotoristas = new HashMap<>();
    public static HashMap<Integer, ArrayList<InterfaceCli>> mapaInteresseClientes = new HashMap<>();

    public static synchronized void adicionaTransfer(TransferModel tm) {
        //Adiciona o transfer na lista geral
        transfers.add(tm);
    }

    public static TransferModel getTransferPorId(int id) {
        for (TransferModel tm : transfers) {
            if (tm.getId() == id) {
                return tm;
            }
        }
        return null;
    }

    public static int getTransferIndicePorId(int id) {
        for (int i = 0; i < transfers.size(); i++) {
            if (transfers.get(i).getId() == id) {
                return i;
            }
        }
        return 0;
    }

    public static synchronized void alteraTransfer(TransferModel tm, String modificacao, String usuario) throws RemoteException {
        //Obtem indice do transfer
        int indice = -1;

        for (int i = 0; i < transfers.size(); i++) {
            if (transfers.get(i).getId() == tm.getId()) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            System.out.println("Transfer " + tm.getId() + " não existe, e não pode ser alterado.");
        } else {
            //Caso exista, realiza a mudança
            transfers.get(indice).change(tm);

            //Insere notificação na fila de acordo com quem realizou a modificacao
            //ex: usuario reservou, entao deve ser notificado ao motorista
            //ex: motorista modificou, deve ser notificado ao cliente
            if (usuario.equalsIgnoreCase("cliente")) {
                filaNotificacoesMotoristas.add(new Notificacao(tm.getId(), modificacao));
            } else if (usuario.equalsIgnoreCase("motorista")) {
                filaNotificacoesClientes.add(new Notificacao(tm.getId(), modificacao));
            }

        }
    }

    public static String getTransfers() {
        //Formata a lista de transfers de maneira legível para o cliente
        StringBuilder sb = new StringBuilder();

        for (TransferModel tm : transfers) {
            //Lista somente os transfers disponíveis (que ainda não foram reservados)
            if (!tm.isReservado()) {
                sb.append("\nTransfer número: " + tm.getId() + "\n");
                sb.append("Data e Hora: " + sdf.format(tm.getDataHora()) + "\n");
                sb.append("Itinerário: " + tm.getItinerario() + "\n");
                sb.append("----------");
            }
        }

        //Remove os traços de divisão antes de retornar
        return sb.toString().substring(0, sb.toString().length() - 10);
    }

    public static String getCotacao(TransferModel tm) {
        //Formata todos os dados do transfer de maneira legível para o cliente
        StringBuilder sb = new StringBuilder();

        sb.append("\nTransfer número: " + tm.getId() + "\n");
        sb.append("Tipo do Veículo: " + tm.getTipoVeiculo() + "\n");
        sb.append("Número de Passageiros: " + tm.getNumPassageiros() + "\n");
        sb.append("Data e Hora: " + sdf.format(tm.getDataHora()) + "\n");
        sb.append("Itinerário: " + tm.getItinerario() + "\n");
        sb.append("Preço: R$" + tm.getPreco() + "\n");

        //Cria uma notificacao para ser enviada ao motorista que fornece este transfer
        filaNotificacoesMotoristas.add(new Notificacao(tm.getId(), "um cliente realizou uma cotação do seu transfer de número " + tm.getId()));

        return sb.toString();
    }

    public static ArrayList<InterfaceMotorista> getMotoristasInteressadosEm(int interesse) {
        //buscar no map a partir do interesse
        return mapaInteresseMotoristas.get(interesse);
    }

    public static synchronized void adicionaInteresseMotorista(int interesse, InterfaceMotorista motorista) {
        //verificar se ja existe alguem com este interesse
        if (!mapaInteresseMotoristas.containsKey(interesse)) {
            //se nao existir, insere (cria nova lista e insere)
            ArrayList<InterfaceMotorista> motoristas = new ArrayList<>();
            motoristas.add(motorista);
            mapaInteresseMotoristas.put(interesse, motoristas);
        } else {
            //se ja existir, pega a lista existente do mapeamento e insere o motorista como interessado
            ArrayList<InterfaceMotorista> motoristas = mapaInteresseMotoristas.get(interesse);
            motoristas.add(motorista);
            //substitui/atualiza a lista de interessados
            mapaInteresseMotoristas.replace(interesse, motoristas);
        }
    }

    public static void enviaNotificacaoInteresseMotoristas(int id, String modificação) throws RemoteException {
        //Encontrar clientes e motoristas que tem interesse neste transfer
        ArrayList<InterfaceMotorista> motoristas = getMotoristasInteressadosEm(id);

        //Se houver pessoas interessadas neste transfer
        if (motoristas != null) {
            //Enviar notificacao para cada um deles
            for (InterfaceMotorista mot : motoristas) {
                mot.receberNotificacao(modificação);
            }
        }
    }

    public static void startNotificacoesMotoristas() {
        //Inicia task
        Timer timer = new Timer();
        timer.schedule(new VerificadorNotificacoesMotoristas(), 0, 1000);
    }

    static class VerificadorNotificacoesMotoristas extends TimerTask {

        public void run() {
            //Verifica se existem novas
            //Se a fila não estiver vazia
            if (!DbManager.filaNotificacoesMotoristas.isEmpty()) {
                try {
                    enviaNotificacoes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public synchronized void enviaNotificacoes() throws RemoteException {
            Notificacao n = DbManager.filaNotificacoesMotoristas.remove();

            //Envia notificação para os interessados
            enviaNotificacaoInteresseMotoristas(n.getId(), n.getModificacao());
        }
    }

    public static void enviaNotificacaoInteresseClientes(int id, String modificação) throws RemoteException {
        //Encontrar clientes e motoristas que tem interesse neste transfer
        ArrayList<InterfaceCli> clientes = getClientesInteressadosEm(id);

        //Se houver pessoas interessadas neste transfer
        if (clientes != null) {
            //Enviar notificacao para cada um deles
            for (InterfaceCli cli : clientes) {
                cli.receberNotificacao("transfer número " + id + " " + modificação);
            }
        }
    }

    public static ArrayList<InterfaceCli> getClientesInteressadosEm(int interesse) {
        //buscar no map a partir do interesse
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

    public static void startNotificacoesClientes() {
        //Inicia task
        Timer timer = new Timer();
        timer.schedule(new VerificadorNotificacoesClientes(), 0, 1000);
    }

    static class VerificadorNotificacoesClientes extends TimerTask {

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
            enviaNotificacaoInteresseClientes(n.getId(), n.getModificacao());
        }
    }
}
