package modelo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class RepositorioArchivo extends RepositorioAsociativo<Equipo, Mantenimiento> {

    private final String nombreArchivo;

    public RepositorioArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public void guardarEnArchivo() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (ParAsociativo<Equipo, Mantenimiento> par : asociaciones) {
                Equipo e = par.getPrimero();
                Mantenimiento m = par.getSegundo();

                String linea = String.join(",",
                        String.valueOf(e.getId()),
                        e.getNombre(),
                        e.getTipo(),
                        String.valueOf(m.getId()),
                        m.getDescripcion(),
                        m.getTecnico(),
                        String.valueOf(m.getFecha()),
                        String.valueOf(m.getCosto())
                );
                bw.write(linea);
                bw.newLine();
            }
        }
    }

    // Método que faltaba
    public void cargarDesdeArchivo() throws IOException {
        asociaciones.clear(); // Limpiamos la lista antes de cargar
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",");

                if (campos.length == 8) { // Validamos la línea
                    Equipo e = new Equipo(
                            Integer.parseInt(campos[0]),
                            campos[1],
                            campos[2]
                    );

                    Mantenimiento m = new Mantenimiento(
                            Integer.parseInt(campos[3]),
                            campos[4],
                            campos[5],
                            LocalDate.parse(campos[6]),
                            Double.parseDouble(campos[7])
                    );

                    asociaciones.add(new ParAsociativo<>(e, m));
                }
            }
        }
    }
}
