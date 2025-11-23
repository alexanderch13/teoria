package modelo;

import java.util.ArrayList;
import java.util.List;

public class RepositorioAsociativo<T, U> {

    protected List<ParAsociativo<T, U>> asociaciones = new ArrayList<>();

    private List<RepositorioListener> listeners = new ArrayList<>();

    public void agregarListener(RepositorioListener l) {
        listeners.add(l);
    }
    
    public void reemplazarTodo(List<? extends ParAsociativo<? extends T, ? extends U>> nuevaLista) {
    	asociaciones.clear();

    	for (ParAsociativo<? extends T, ? extends U> p : nuevaLista) {
            asociaciones.add(new ParAsociativo<>(p.getPrimero(), p.getSegundo()));
    	}

    	notificar("Inventario reemplazado desde archivo externo");
    }


    public void notificar(String evento) {
        for (RepositorioListener l : listeners) {
            l.onRepositorioEvent(evento);
        }
    }

    public void agregar(T t, U u) {
        asociaciones.add(new ParAsociativo<>(t, u));
        notificar("Nuevo par agregado");
    }

    public List<ParAsociativo<T, U>> listar() {
        return new ArrayList<>(asociaciones);
    }
}

