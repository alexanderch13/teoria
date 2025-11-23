
package modelo.orden;

import java.util.Comparator;
import modelo.*;

public class OrdenPorFecha implements Comparator<ParAsociativo<Equipo, Mantenimiento>> {

    @Override
    public int compare(
        ParAsociativo<Equipo, Mantenimiento> p1,
        ParAsociativo<Equipo, Mantenimiento> p2
    ) {
        return p1.getSegundo().getFecha().compareTo(p2.getSegundo().getFecha());
    }
}
