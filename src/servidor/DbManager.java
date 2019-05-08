package servidor;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author cnmoro
 */
public class DbManager {

    public static ArrayList<TransferModel> transfers = new ArrayList<>();
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static Queue<Notificacao> filaNotificacoesClientes = new LinkedList<>();
    public static Queue<Notificacao> filaNotificacoesMotoristas = new LinkedList<>();

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

    public static synchronized void alteraTransfer(TransferModel tm, String modificacao) throws RemoteException {
        //Obtem indice do transfer
        int indice = transfers.indexOf(tm);
        //Se indice for -1, significa que não existe, e não realiza nenhuma ação
        if (indice == -1) {
            System.out.println("Este transfer não existe, e não pode ser alterado.");
        } else {
            //Caso exista, realiza a mudança
            transfers.get(indice).change(tm);

            //Insere notificação na fila
            filaNotificacoesClientes.add(new Notificacao(tm.getId(), modificacao));
            filaNotificacoesMotoristas.add(new Notificacao(tm.getId(), modificacao));
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

        return sb.toString();
    }

    /*
    public static synchronized void adicionaInteresseMotorista(int interesse, InterfaceMotorista motorista) {
        //verificar se ja existe alguem com este interesse
        if (!mapaInteresseMotoristas.containsKey(interesse)) {
            //se nao existir, insere (cria nova lista e insere)
            ArrayList<InterfaceMotorista> motoristas = new ArrayList<>();
            motoristas.add(motorista);
            mapaInteresseMotoristas.put(interesse, motoristas);
        } else {
            //se ja existir, pega a lista existente do mapeamento e insere o cliente como interessado
            ArrayList<InterfaceMotorista> motoristas = mapaInteresseMotoristas.get(interesse);
            motoristas.add(motorista);
            //substitui/atualiza a lista de interessados
            mapaInteresseMotoristas.replace(interesse, motoristas);
        }
    }

    public static ArrayList<InterfaceMotorista> getMotoristasInteressadosEm(int interesse) {
        //buscar no map a partir do interesse
        return mapaInteresseMotoristas.get(interesse);
    }
     */
}
