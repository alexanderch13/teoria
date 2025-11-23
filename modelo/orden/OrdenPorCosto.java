
package modelo.orden;

import java.util.Comparator;
import modelo.*;

public class OrdenPorCosto implements Comparator<ParAsociativo<Equipo, Mantenimiento>> {

    @Override
    public int compare(
        ParAsociativo<Equipo, Mantenimiento> p1,
        ParAsociativo<Equipo, Mantenimiento> p2
    ) {
        return Double.compare(
            p1.getSegundo().getCosto(),
            p2.getSegundo().getCosto()
        );
    }
}
