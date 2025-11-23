package modelo.presentacion;

import modelo.*;

public class PresentacionCompacta implements EstrategiaListado {

    @Override
    public String representar(ParAsociativo<Equipo, Mantenimiento> par) {
        return par.getPrimero().getNombre();
    }
}

