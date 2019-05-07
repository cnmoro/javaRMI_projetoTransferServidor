package servidor;

import java.util.Date;

/**
 *
 * @author cnmoro
 */
public class TransferModel {

    int id;
    double preco;
    int numPassageiros;
    String tipoVeiculo;
    Date dataHora;
    String itinerario;

    public TransferModel() {
    }

    public TransferModel(int id, double preco, int numPassageiros, String tipoVeiculo, Date dataHora, String intinerario) {
        this.id = id;
        this.preco = preco;
        this.numPassageiros = numPassageiros;
        this.tipoVeiculo = tipoVeiculo;
        this.dataHora = dataHora;
        this.itinerario = intinerario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getNumPassageiros() {
        return numPassageiros;
    }

    public void setNumPassageiros(int numPassageiros) {
        this.numPassageiros = numPassageiros;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public String getItinerario() {
        return itinerario;
    }

    public void setItinerario(String intinerario) {
        this.itinerario = intinerario;
    }

    public void change(TransferModel tm) {
        this.dataHora = tm.getDataHora();
        this.itinerario = tm.getItinerario();
        this.numPassageiros = tm.getNumPassageiros();
        this.preco = tm.getPreco();
        this.tipoVeiculo = tm.getTipoVeiculo();
    }

}
