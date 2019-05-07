package servidor;

import interfaces.InterfaceCli;
import interfaces.InterfaceMotorista;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author cnmoro
 */
public class DbManager {

    public static List<TransferModel> transfers = new ArrayList<>();
    public static HashMap<String, ArrayList<InterfaceCli>> mapaInteresseClientes = new HashMap<>();
    public static HashMap<String, ArrayList<InterfaceMotorista>> mapaInteresseMotoristas = new HashMap<>();

    public static synchronized void adicionaTransfer(TransferModel tm) {
        transfers.add(tm);
    }

    public static synchronized void alteraTransfer(TransferModel tm) {
        int indice = transfers.indexOf(tm);
        if (indice == -1) {
            System.out.println("Este transfer não existe, e não pode ser alterado.");
        } else {
            transfers.get(indice).change(tm);
        }
    }

    public static String getTransfers() {
        //Formata a lista de transfer de maneira legível para o cliente
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        for (TransferModel tm : transfers) {
            sb.append("\nTransfer número: " + tm.getId() + "\n");
            sb.append("Tipo do Veículo: " + tm.getTipoVeiculo() + "\n");
            sb.append("Número de Passagens: " + tm.getNumPassageiros() + "\n");
            sb.append("Data e Hora: " + sdf.format(tm.getDataHora()) + "\n");
            sb.append("Itinerário: " + tm.getItinerario() + "\n");
            sb.append("Preço: R$" + tm.getPreco() + "\n");
            sb.append("----------\n\n");
        }

        return sb.toString();
    }

    public static synchronized void adicionaInteresseCliente(String interesse, InterfaceCli cliente) {
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

    public static synchronized void adicionaInteresseMotorista(String interesse, InterfaceMotorista motorista) {
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

    public static ArrayList<InterfaceCli> getClientesInteressadosEm(String interesse) {
        //buscar no map a partir do interesse
        return mapaInteresseClientes.get(interesse);
    }

    public static ArrayList<InterfaceMotorista> getMotoristasInteressadosEm(String interesse) {
        //buscar no map a partir do interesse
        return mapaInteresseMotoristas.get(interesse);
    }

}
