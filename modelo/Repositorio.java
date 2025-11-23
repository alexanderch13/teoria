
package modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class Repositorio implements SujetoInventario {

    protected List<ParAsociativo<Equipo, Mantenimiento>> lista = new ArrayList<>();
    private List<ObservadorInventario> listeners = new ArrayList<>();

    @Override
    public void agregarListener(ObservadorInventario obs) {
        listeners.add(obs);
    }

    @Override
    public void removerListener(ObservadorInventario obs) {
        listeners.remove(obs);
    }

    @Override
    public void notificarATodos(String evento) {
        for (ObservadorInventario o : listeners) {
            o.notificar(evento);
        }
    }


    public void registrarPar(ParAsociativo<Equipo, Mantenimiento> par) {
        lista.add(par);
        notificarATodos("Nuevo par registrado: " + par.getPrimero().getNombre());
    }

    public void eliminarPar(ParAsociativo<Equipo, Mantenimiento> par) {
        lista.remove(par);
        notificarATodos("Par eliminado: " + par.getPrimero().getNombre());
    }

    public void limpiar() {
        lista.clear();
        notificarATodos("Inventario limpiado.");
    }


    public void reemplazarTodo(List<ParAsociativo<Equipo, Mantenimiento>> nuevaLista) {
    	this.lista.clear();
    	this.lista.addAll(nuevaLista);
    	notificarATodos("Inventario reemplazado desde archivo.");
    }
    public abstract void cargarDesdeArchivo() throws Exception;
    public abstract void guardarEnArchivo() throws Exception;

    public List<ParAsociativo<Equipo, Mantenimiento>> listar() {
        return lista;
    }
}
