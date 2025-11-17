package examen;

import java.util.ArrayList;
import java.util.List;

public class RepositorioAsociativo<T, U> {
    protected List<ParAsociativo<T, U>> asociaciones = new ArrayList<>();

    public void agregar(T t, U u) {
        asociaciones.add(new ParAsociativo<>(t, u));
    }

    public List<ParAsociativo<T, U>> listar() {
        return new ArrayList<>(asociaciones);
    }
}
