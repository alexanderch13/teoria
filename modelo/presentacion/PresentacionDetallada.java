
package modelo.presentacion;

import modelo.*;

public class PresentacionDetallada implements EstrategiaListado {

    @Override
    public String representar(ParAsociativo<Equipo, Mantenimiento> par) {
        return par.toString();
    }
}

