
package modelo;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class RepositorioArchivo extends Repositorio {

    private String rutaArchivo;

    public RepositorioArchivo(String ruta) {
        this.rutaArchivo = ruta;
    }

    public void cargarDesdeArchivo() throws Exception {
        List<ParAsociativo<Equipo, Mantenimiento>> temp = new ArrayList<>();

        File f = new File(rutaArchivo);
        if (!f.exists()) return;

        BufferedReader br = new BufferedReader(new FileReader(f));
        String linea;

        while ((linea = br.readLine()) != null) {
            String[] p = linea.split(";");
            if (p.length != 8) continue;

            Equipo e = new Equipo(
                    Integer.parseInt(p[0]),
                    p[1], p[2]);

            Mantenimiento m = new Mantenimiento(
                    Integer.parseInt(p[3]),
                    p[4], p[5],
                    LocalDate.parse(p[6]),
                    Double.parseDouble(p[7])
            );

            temp.add(new ParAsociativo<>(e, m));
        }
        br.close();

        reemplazarTodo(temp);
    }

    public void guardarEnArchivo() throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo));

        for (ParAsociativo<Equipo, Mantenimiento> par : lista) {
            Equipo e = par.getPrimero();
            Mantenimiento m = par.getSegundo();

            bw.write(e.getId() + ";" + e.getNombre() + ";" + e.getTipo() + ";"
                    + m.getId() + ";" + m.getDescripcion() + ";" + m.getTecnico() + ";"
                    + m.getFecha() + ";" + m.getCosto());
            bw.newLine();
        }

        bw.close();
        notificarATodos("Archivo guardado");
    }
}

