package modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RepositorioBD {

    private static final String URL = "jdbc:sqlite:/home/dyth/Documentos/Lenguaje/Teo/teoria/data.db";

    public boolean guardarAsociacionesEnBD(List<ParAsociativo<Equipo, Mantenimiento>> asociaciones)
            throws SQLException {

        try (Connection con = DriverManager.getConnection(URL)) {

            for (ParAsociativo<Equipo, Mantenimiento> par : asociaciones) {

                Equipo e = par.getPrimero();
                Mantenimiento m = par.getSegundo();

                String checkEquipo = "SELECT COUNT(*) FROM equipos WHERE id = ?";

                try (PreparedStatement psCheck = con.prepareStatement(checkEquipo)) {

                    psCheck.setInt(1, e.getId());
                    ResultSet rs = psCheck.executeQuery();
                    rs.next();
                    boolean existe = rs.getInt(1) > 0;

                    if (!existe) {
                        String insertEquipo = "INSERT INTO equipos (id, nombre, tipo) VALUES (?, ?, ?)";

                        try (PreparedStatement psInsert = con.prepareStatement(insertEquipo)) {
                            psInsert.setInt(1, e.getId());
                            psInsert.setString(2, e.getNombre());
                            psInsert.setString(3, e.getTipo());

                            psInsert.executeUpdate();
                        }
                    }
                }

                String insertMantenimiento = """
                        INSERT INTO mantenimientos (equipo_id, descripcion, tecnico, fecha, costo)
                        VALUES (?, ?, ?, ?, ?)
                        """;

                try (PreparedStatement psM = con.prepareStatement(insertMantenimiento)) {
                    psM.setInt(1, e.getId());
                    psM.setString(2, m.getDescripcion());
                    psM.setString(3, m.getTecnico());
                    psM.setDate(4, java.sql.Date.valueOf(m.getFecha()));
                    psM.setDouble(5, m.getCosto());

                    psM.executeUpdate();
                }
            }
        }

        return true;
    }
}
