import java.io.Serializable;

public class ParAsociativo<T,U> implements Serializable
{
  private static final long serialVersionUID = 1L;
  private T primero;
  private U segundo;

  public ParAsociativo(T primero, U segundo)
  {
    this.primero=primero;
    this.segundo=segundo;
  }

  public T getPrimero()
  {
    return primero;
  }
  
  public U getSegundo()
  {
    return segundo;
  }

  @Override
  public String toString()
  {
    return primero.toString() + "--->" + segundo.toString();
  }
}
