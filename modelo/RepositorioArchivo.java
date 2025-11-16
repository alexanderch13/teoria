import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RepositorioArchivo extends RepositorioAsociativo<Equipo, Mantenimiento>
{
  private final String nombreArchivo;
  
  public RepositorioArchivo(String nombreArchivo)
  {
    this.nombreArchivo=nombreArchivo;
  }

  public void guardarEnArchivo() throws IOException
  {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo)))
    {
      for (ParAsociativo<Equipo, Mantenimiento> par : asociaciones)
      {
        Equipo e = par.getPrimero();
        Mantenimiento m = par.getSegundo();
        
        String linea = String.join (",",
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
}
