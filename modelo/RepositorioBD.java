public class RepositorioBD {

    private static final String URL = "jdbc:sqlite:/Users/karim/Downloads/BD/prueba.db";

    public boolean guardarAsociacionesEnBD(List<ParAsociado<Equipo, Mantenimiento>> asociaciones)
            throws SQLException {

        try (Connection con = DriverManager.getConnection(URL)) {

            for (ParAsociado<Equipo, Mantenimiento> par : asociaciones) {

                Equipo e = par.getPrimero();
                Mantenimiento m = par.getSegundo();

                String checkEquipo = "SELECT COUNT(*) FROM equipos WHERE id = ?";

                try (PreparedStatement psCheck = con.prepareStatement(checkEquipo)) {

                    psCheck.setInt(1, e.getId());
                    ResultSet rs = psCheck.executeQuery();
                    rs.next();
                    boolean existe = rs.getInt(1) > 0;

                    if (!existe) {

                        String insertEquipo = "INSERT INTO Equipos (id, nombre, tipo) "
                                + "VALUES (?, ?, ?)";

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
