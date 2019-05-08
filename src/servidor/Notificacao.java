package servidor;

/**
 *
 * @author cnmoro
 */
public class Notificacao {

    int id;
    String modificacao;

    public Notificacao() {
    }

    public Notificacao(int id, String modificacao) {
        this.id = id;
        this.modificacao = modificacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModificacao() {
        return modificacao;
    }

    public void setModificacao(String modificacao) {
        this.modificacao = modificacao;
    }

}
