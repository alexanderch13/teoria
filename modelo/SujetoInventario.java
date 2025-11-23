
package modelo;

public interface SujetoInventario {

    void agregarListener(ObservadorInventario obs);

    void removerListener(ObservadorInventario obs);

    void notificarATodos(String evento);
}
