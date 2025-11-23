
package modelo.orden;

import java.util.Comparator;
import modelo.*;

public class OrdenPorTecnico implements Comparator<ParAsociativo<Equipo, Mantenimiento>> {

    @Override
    public int compare(
        ParAsociativo<Equipo, Mantenimiento> p1,
        ParAsociativo<Equipo, Mantenimiento> p2
    ) {
        return p1.getSegundo().getTecnico()
            .toLowerCase()
            .compareTo(p2.getSegundo().getTecnico().toLowerCase());
    }
}
