
package controlador;

import modelo.*;
import java.util.List;
import java.sql.SQLException;

public class ControladorInventario {

    private RepositorioArchivo repoArchivo;
    private RepositorioBD repoBD;

    public ControladorInventario(String archivo) {
        this.repoArchivo = new RepositorioArchivo(archivo);
        this.repoBD = new RepositorioBD();

    }

    public void registrarAsociacion(Equipo e, Mantenimiento m) {
        ParAsociativo<Equipo, Mantenimiento> par = new ParAsociativo<>(e, m);
        repoArchivo.registrarPar(par);
    }

    public void eliminarAsociacion(ParAsociativo<Equipo, Mantenimiento> par) {
        repoArchivo.eliminarPar(par);
    }

    public List<ParAsociativo<Equipo, Mantenimiento>> listarAsociaciones() {
        return repoArchivo.listar();
    }

    public void limpiarInventario() {
        repoArchivo.limpiar();
    }

    public boolean guardarArchivo() {
        try {
            repoArchivo.guardarEnArchivo();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean guardarEnBD() {
        try {
            return repoBD.guardarAsociacionesEnBD(listarAsociaciones());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public RepositorioArchivo getRepositorioArchivo() { 
	return repoArchivo; 
    }
    public RepositorioBD getRepositorioBD() {
	return repoBD;

    }
}

