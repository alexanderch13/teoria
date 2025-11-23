
package modelo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EstrategiaPorTecnico implements EstrategiaListado {

    @Override
    public String representar(ParAsociativo<Equipo, Mantenimiento> par) {
        Mantenimiento m = par.getSegundo();
        Equipo e = par.getPrimero();
        return m.getTecnico() + " -> " + e.getNombre();
    }

    public List<ParAsociativo<Equipo, Mantenimiento>> ordenar(List<ParAsociativo<Equipo, Mantenimiento>> lista) {
        return lista.stream()
            .sorted(Comparator.comparing(o -> o.getSegundo().getTecnico()))
            .collect(Collectors.toList());
    }
}
