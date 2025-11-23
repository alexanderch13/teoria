
package modelo;

public class EstrategiaCompacta implements EstrategiaListado {
    @Override
    public String representar(ParAsociativo<Equipo, Mantenimiento> par) {
        Equipo e = par.getPrimero();
        return "[EQ] " + e.getNombre();
    }
}
