
package modelo;

public class EstrategiaDetallada implements EstrategiaListado {
    @Override
    public String representar(ParAsociativo<Equipo, Mantenimiento> par) {
        Equipo e = par.getPrimero();
        Mantenimiento m = par.getSegundo();

        return "Equipo: " + e.getId() + "," + e.getNombre() + "," + e.getTipo() +
               "  |  Mant: " + m.getId() + "," + m.getDescripcion() + "," +
               m.getTecnico() + "," + m.getFecha() + "," + m.getCosto();
    }
}
