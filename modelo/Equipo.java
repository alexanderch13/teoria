package modelo;

public class Equipo
{
  private int id;
  private String nombre;
  private String tipo;

  public Equipo(int id,String nombre, String tipo)
  {
    this.id=id;
    this.nombre=nombre;
    this.tipo=tipo;
  }

  public int getId()
  {
    return id;
  }
  public String getNombre()
  {
    return nombre;
  }
  public String getTipo()
  {
    return tipo;
  }

  @Override
  public String toString()
  {
	  return id + " - " + nombre + " (" + tipo + ")";
  }
}
