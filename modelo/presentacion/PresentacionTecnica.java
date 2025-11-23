
package modelo.presentacion;

import modelo.*;

public class PresentacionTecnica implements EstrategiaListado {

    @Override
    public String representar(ParAsociativo<Equipo, Mantenimiento> par) {
        Equipo e = par.getPrimero();
        Mantenimiento m = par.getSegundo();

        return String.format(
            "ID:%d | %s | Tipo:%s | Mant:%s | Tec:%s | %s | %.2f",
            e.getId(), e.getNombre(), e.getTipo(),
            m.getDescripcion(), m.getTecnico(), m.getFecha(),
            m.getCosto()
        );
    }
}
