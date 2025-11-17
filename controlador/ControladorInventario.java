//hacer el examen, modelo, controlador, falta la vista gráfica. opciones: 
//
//registrar un par (equipo, mantenimiento) escribir el id, nom, tipo .. etc -> nuevo par en lista de asociaciones
//
//
//ver todos los pares en lista de asociaciones 
//
//
//guardar los pares de la lista de asociaciones en el archivo. informacion del archivo tambien
//
//
//recuperar la informacion en el archivo a la lista de asociaciones
//
//
//guardar la lista de asociaciones a mi base de datos y permitir salir de la aplicacion.
//
//equipo: id,nom,tipo (id pk)
//
//mantenimiento: idM,descripcion,tec,fec, (idE fk)
package examen;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorInventario {

    private RepositorioArchivo repositorio;
    private RepositorioBD repositorioBD;

    public ControladorInventario(String archivo) {
        this.repositorio = new RepositorioArchivo(archivo);
        this.repositorioBD = new RepositorioBD();
    }

    public void registrarAsociacion(Equipo e, Mantenimiento m) {
        repositorio.agregar(e, m);
    }

    public List<ParAsociativo<Equipo, Mantenimiento>> listarAsociaciones() {
        return repositorio.listar();
    }

    public boolean guardarArchivo() {
        try {
            repositorio.guardarEnArchivo();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean cargarArchivo() {
        try {
            repositorio.cargarDesdeArchivo();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean guardarEnBD() {
        try {
            return repositorioBD.guardarAsociacionesEnBD(listarAsociaciones());
        } catch (SQLException e) {
            return false;
        }
    }
}
